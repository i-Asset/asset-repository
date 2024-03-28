using System.Configuration;
using System.Collections.Specialized;
// using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Net.Security;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using System.Web;
using Newtonsoft.Json.Linq;
using RestSharp;
using Microsoft.OpenApi.Models;
using Newtonsoft.Json;
using testRESTbackend.Classes;
using JsonSerializer = System.Text.Json.JsonSerializer;


namespace testRESTbackend
{
    public class Backend
    {
        private static void OutputResponse(RestResponse response)
        {
            Console.WriteLine();
            if (response.IsSuccessful)
            {
                Console.WriteLine("Response: ");
                Console.WriteLine(response.StatusCode);
                Console.WriteLine(response.Content);
            }
            else
            {
                Console.WriteLine("Something went wrong...");
                Console.WriteLine("{0} [{1}]", response.Content, response.StatusCode);
                Console.WriteLine();
                Console.WriteLine(response.ErrorException);
            }
            return;
        }

        public static RestResponse GetEligibilityToken(bool verbose = false)
        {
            ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;
            var config = new ConfigContainer();
            
            var client = new RestClient(config.TokenEndpoint);
            var request = new RestRequest(config.TokenEndpoint, Method.Post);
            request.AddHeader("cache-control", "no-cache");
            request.AddHeader("content-type", "application/x-www-form-urlencoded");
            request.AddParameter("grant_type", config.GrantType);
            request.AddParameter("scope", config.Scope);
            request.AddParameter("client_id", config.ClientId);
            request.AddParameter("client_secret", config.ClientSecret);

            RestResponse response = client.Execute(request);
            if (verbose)
            {
                if (response.IsSuccessful)
                {
                    Console.WriteLine($"Token OK: {response.Content}");
                }
                else
                {
                    Console.WriteLine("Token error !");
                    OutputResponse(response);
                }
            }

            return response;
        }
        
        public static string GetJsonParameter(RestResponse response, string parameter)
        {
            var obj = JObject.Parse(response.Content);
            var value = obj.SelectToken(parameter);
            if (value != null)
            {
                return (string)value;
            } 
            return String.Empty;
        }

        public static async Task<RestResponse> GraphqlQuery(string query)
        {
            var config = new ConfigContainer();
            
            //TODO: Token refresh / check if token is still valid
            var token = GetEligibilityToken();
            var client = new RestClient(config.GraphqlEndpoint);
            var tokenStr = "Bearer " + GetJsonParameter(token, "access_token");
            var request = new RestRequest(config.GraphqlEndpoint, Method.Post);
            
            request.AddHeader("Authorization", tokenStr);
            request.AddHeader("Content-Type", "application/json");
            request.AddParameter("application/json", query, ParameterType.RequestBody);
            
            RestResponse response = await client.ExecuteAsync(request);
            if (!response.IsSuccessful)
            {
                Console.WriteLine("Query-Response Error: ");
                OutputResponse(response);
                return null;
            }

            return response;
        }
        
        
        public static Task<GraphqlResponse> GraphqlEquipmentQuery(string parentGUID = "")
        {
            ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls13;
            var config = new ConfigContainer();
            
            var parentGUIDString = "";
            if (parentGUID != "")
            {
                parentGUIDString = $", parents: \\\"{parentGUID}\\\"";
            }
            string equipmentQuery = 
                "{\"query\":\"query{\\r\\n" +
                    $"equipmentGroups( database: \\\"{config.GraphqlDb}\\\"{parentGUIDString})\\r\\n" +
                    "\\t{\\r\\n" +
                        "\\t\\tname\\r\\n" +
                        "\\t\\tguid\\r\\n" +
                        "\\t\\tdescription\\r\\n" +
                    "\\t}" +
                "}\"" +
                ",\"variables\":{}}";

            var response = GraphqlQuery(equipmentQuery).Result;
            // GraphqlResponse graphqlResponse = JsonConvert.DeserializeObject<GraphqlResponse>(response.Content);
            GraphqlResponse graphqlResponse = JsonSerializer.Deserialize<GraphqlResponse>(response.Content);
            
            return Task.FromResult(graphqlResponse);
        }
        

        //Outputs equipment tree to console and returns the depth of the deepest layer for future GraphQL queries
        public static async Task<int> IterateEquipmentTree(GraphqlResponse currentLayer, bool consoleOutput = true, int layerNum = 0)
        {
            var maxLayer = layerNum;
            
            //root layer only
            if (currentLayer == null)
            {
                currentLayer = await GraphqlEquipmentQuery();
            }
            //Normal non-root layer
            foreach (var equipment in currentLayer.data.equipmentGroups)
            {
                var equipmentGUID = equipment.guid;
                if (consoleOutput)
                {
                    for (int i = 0; i < layerNum; i++)
                    {
                        Console.Write("\t");
                    }
                    Console.WriteLine(equipment.name);
                }
                var depth = await IterateEquipmentTree(await GraphqlEquipmentQuery(equipmentGUID), consoleOutput, layerNum + 1);
                
                //Count depth of deepest layer to facilitate accurate GraphQL queries if needed
                if (depth > maxLayer)
                {
                    maxLayer = depth;
                }
            }

            return maxLayer;
        }
        
        public static async Task<int> GetEquipmentTreeDepth()
        {
            var depth = await IterateEquipmentTree(null, false);
            return depth;
        }
        
        public static async Task<string> EquipmentFullQuery()
        {
            var config = new ConfigContainer();
            var depth = await GetEquipmentTreeDepth();
            
            var queryStart =
                "{\"query\":\"query{\\r\\n" +
                $"equipmentGroups( database: \\\"{config.GraphqlDb}\\\"){{\\r\\n" +
                    "name\\r\\n" +
                    "guid\\r\\n" +
                    "description\\r\\n" +
                    "linkedVariables{\\r\\n" +
                        "variableName\\r\\n" +
                        "variableName\\r\\n" +
                        "displayName\\r\\n" +
                        "identification\\r\\n" +
                    "}\\r\\n";

                string subQuery = "";
            for (int i = 0; i < depth; i++)
            {
               subQuery += "childEquipmentGroups{\\r\\n" +
                    "name\\r\\n" +
                    "guid\\r\\n" +
                    "description\\r\\n" +
                    "linkedVariables{\\r\\n" +
                        "variableName\\r\\n" +
                        "variableName\\r\\n" +  
                        "displayName\\r\\n" +
                        "identification\\r\\n" +
                    "}\\r\\n";
            }

            string queryEnd = "";
            for (int i = 0; i < depth; i++)
            {
                queryEnd += "}";
            }
            queryEnd += "}}\",\"variables\":{}}";
             
            string query = queryStart + subQuery + queryEnd;
            Console.WriteLine(query);
            var response = GraphqlQuery(query);
            
            return response.Result.Content;
        }

        // gets live alarm data from zenon runtime
        public static Task<string?> AlarmDataQuery(int seconds = 20)
        {
            ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls13;
            var config = new ConfigContainer();
            
            string equipmentQuery = 
                "{\"query\":\"query{\\r\\n" +
                    $"alarmData(database: \\\"{config.GraphqlDb}\\\", project: \\\"{config.ZenonProject}\\\", startTime: \\\"{DateTime.UtcNow.AddSeconds(-seconds).ToString("s")}\\\", endTime: \\\"{DateTime.UtcNow.ToString("s")}\\\"){{\\r\\n" +
                        "variable{variableName},\\r\\n" +
                        "alarmText,\\r\\n" +
                        "alarmClass{name},\\r\\n" +
                        "alarmGroup{name},\\r\\n" +
                        "timeComes,\\r\\n" +
                        "timeGoes,\\r\\n" +
                    "}" +
                "}\"}";

            try
            {
                var response = GraphqlQuery(equipmentQuery).Result;
                //GraphqlResponse graphqlResponse = JsonConvert.DeserializeObject<GraphqlResponse>(response.Content);
                
                // Flatten the JSON by converting str->obj->flatten->str, so that zenonConnector can properly may values (nested values were causing stealthy issues)
                var responseJSON_obj = JsonConvert.DeserializeObject<Root>(response.Content);
                var Helper = new JsonHelper_Alarms();
                var responseJSON_obj_flat = Helper.Flatten(responseJSON_obj.data.alarmData);
                string responseJSON_string_flat = JsonSerializer.Serialize(responseJSON_obj_flat);
                // return Task.FromResult(response.Content);
                return Task.FromResult("{\"data\":{\"alarmData\":" + responseJSON_string_flat + "}}");
            }
            catch(Exception e)
            {
                Console.WriteLine(e.ToString());
                return Task.FromResult<string>(null);
            }
        }
        
        
        // gets variable info from zenon runtime
        public static Task<string?> VariablesQuery()
        {
            // query{
            //     variables(database: "project_DB", projects: "ZENON11_DEMO"){
            //         variableName,
            //         measuringUnit,
            //         dataType
            //     }
            // }
            
            ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls13;
            var config = new ConfigContainer();

            string varsQuery =
                "{\"query\":\"query{\\r\\n" +
                $"variables(database: \\\"{config.GraphqlDb}\\\", projects: \\\"{config.ZenonProject}\\\"){{\\r\\n" +
                "variableName,\\r\\n" +
                "displayName,\\r\\n" +
                "identification,\\r\\n" +
                "description,\\r\\n" +
                "dataType,\\r\\n" +
                "resourcesLabel,\\r\\n" +
                "measuringUnit,\\r\\n" +
                "}" +
                "}\"}";

        try

        {
                var response = GraphqlQuery(varsQuery).Result;
                return Task.FromResult(response.Content);
            }
            catch(Exception e)
            {
                Console.WriteLine(e.ToString());
                return Task.FromResult<string>(null);
            }
        }
        
        
        // gets data of one (URL-specified) archive from zenon runtime
        // test with http://localhost:5046/archive?archive=OS&startTime=2024-01-01T01:00:00&endTime=2024-02-02T01:00:00
        public static Task<string?> ArchiveQuery(string archive, string startTime, string endTime)
        {
            ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls13;
            var config = new ConfigContainer();

            string archiveQuery = "{\"query\":\"query{\\r\\n" +
                                  $"archiveData(database: \\\"{config.GraphqlDb}\\\", \\r\\n" +
                                  $"project: \\\"{config.ZenonProject}\\\", \\r\\n" + 
                                  $"archive: \\\"{archive}\\\", \\r\\n" + 
                                  $"startTime: \\\"{startTime}\\\", \\r\\n" + 
                                  $"endTime: \\\"{endTime}\\\"){{\\r\\n" +
                                  "timestamp,\\r\\n" +
                                  "stringValue,\\r\\n" +
                                  "numericValue,\\r\\n" +
                                  "calculation,\\r\\n" +
                                  "variable{\\r\\n" +
                                  "variableName,\\r\\n" +
                                  "dataType\\r\\n" +
                                  "}\\r\\n" + 
                                  "}\\r\\n" + 
                                  "}\"}";

            try

            {
                var response = GraphqlQuery(archiveQuery).Result;
                
                // Flatten the JSON by converting str->obj->flatten->str, so that zenonConnector can properly parse values (nested values were causing stealthy issues)
                var responseJSON_obj = JsonConvert.DeserializeObject<ArchiveRoot>(response.Content);
                // var responseJSON_obj = JsonSerializer.Deserialize<ArchiveRoot>(response.Content);
                var Helper = new JsonHelper_Archives();
                var responseJSON_obj_flat = Helper.Flatten(responseJSON_obj.data.archiveData);
                string responseJSON_string_flat = JsonSerializer.Serialize(responseJSON_obj_flat);
                return Task.FromResult("{\"data\":{\"archiveData\":" + responseJSON_string_flat + "}}");
                
                // Console.WriteLine(archiveQuery);
                // return Task.FromResult(response.Content);
            }
            catch(Exception e)
            {
                Console.WriteLine(e.ToString());
                return Task.FromResult<string>(null);
            }
        }
        
        

        // query{
        //     archives(database: "project_DB", projects: "ZENON11_DEMO"){
        //         longName,
        //         shortName
        //         aggregatedArchives{
        //             shortName,
        //             description,
        //         },
        //         variables{
        //             variable{
        //                 variableName
        //             }
        //             calculation
        //         }
        //     }
        // }
        
        
        // public static async Task<string> AlarmDataQuery()
        // {
        //     // Frontend polls /alarmdata
        //     // /alarmdata displays null value but starts alarmdata query in background
        //     // frontend keeps polling (maybe until 5min timeout)
        //     // when alarmdata query is finished, /alarmdata displays data
        //     // /alarmdata displays null value again after frontend polled again and retrieved data or after timeout
        //     // alarmdata query is not started again until frontend polls /alarmdata again
        //     
        //     // OR:
        //     
        //     // /alarmdata refreshes every 2min or so with ALL alarmdata (..better prob. because need to query all variables anyway)
        //     // and frontend can poll whenever
        //     // if no data for 5min or so, /alarmdata can display nullvalue   
        //     
        //     var config = new ConfigContainer();
        //     
        //     while (true)
        //     {
        //         string currTimeStr = DateTime.UtcNow.ToString("s", System.Globalization.CultureInfo.InvariantCulture);
        //         // last 5min of alarms
        //         string startTimeStr = DateTime.UtcNow.AddMinutes(-5).ToString("s", System.Globalization.CultureInfo.InvariantCulture);
        //         
        //         var query = 
        //             "{\"query\":\"query{\\r\\n" +
        //             $"alarmData( database: \\\"{config.GraphqlDb}\\\"){{\\r\\n" +
        //             "name\\r\\n" +
        //             "guid\\r\\n" +
        //             "description\\r\\n" +
        //             "linkedVariables{\\r\\n" +
        //             "variableName\\r\\n" +
        //             "variableName\\r\\n" +
        //             "displayName\\r\\n" +
        //             "identification\\r\\n" +
        //             "}\\r\\n" +
        //             "}}\",\"variables\":{}}";
        //
        //         
        //         int i;
        //         bool success = int.TryParse(config.AlarmRefreshInterval, out i);
        //         int refreshInterval = 180 * 1000; //fallback in case configvalue is invalid integer
        //         if (success)
        //         {
        //             refreshInterval = int.Parse(config.AlarmRefreshInterval) * 1000; ;
        //         }
        //         await Task.Delay(refreshInterval);
        //         
        //         var response = GraphqlQuery(query).Result;
        //         if (response.IsSuccessful)
        //         {
        //             return response.Content;
        //         }
        //     }
        //
        //     return "N/A";
        // }
        
    }
}