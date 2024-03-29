using System.Net;
using Newtonsoft.Json.Linq;
using RestSharp;
using Newtonsoft.Json;
using testRESTbackend.Classes;
using JsonSerializer = System.Text.Json.JsonSerializer;



namespace testRESTbackend
{
    public class Backend
    {
        
        /* Function OutputResponse()
         * <summary> Prints the response of a REST request to the console.
         *           Helper function for debugging.
         * <param> RestResponse response: Response object to be printed
         * <returns> None
         */
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

        
        /* Function GetEligibilityToken()
         * <summary> Retrieves an eligibility token from the IIOT Services.
         *           Used to authenticate GraphQL queries.
         * <param> bool verbose: Whether to print the status information to the console
         * <returns> RestResponse: Response object containing the token
         */
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
        
        
        /* Function GetJsonParameter()
         * <summary> Parses a JSON response and returns a specific parameter
         *           Helper function used to extract JSON data.
         * <param> RestResponse response: Response object containing the JSON data
         * <param> string parameter: Parameter to be extracted from the JSON data
         * <returns> string: Value of the parameter
         */
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

        
        /* Function GraphqlQuery()
         * <summary> Queries the zenon runtime via GraphQL
         *           Helper function for all other functions that make GraphQL queries.   
         * <param> string query: GraphQL query string
         * <returns> Task<RestResponse>: Response object containing the query result
         */
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
        
        
        /* Function GraphqlEquipmentQuery()
         * <summary> Queries the zenon runtime for equipment data via GraphQL
         *           Helper function for other functions that need equipment data.
         * <param> string parentGUID: GUID of the parent equipment group
         * <returns> Task<GraphqlResponse>: JSON string containing equipment data
         */
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
            GraphqlResponse graphqlResponse = JsonSerializer.Deserialize<GraphqlResponse>(response.Content);
            
            return Task.FromResult(graphqlResponse);
        }
        

        /* Function IterateEquipmentTree()
         * <summary> Iterates recursively through the zenon equipment tree via GraphQL.
         *           Can print the equipment tree to the console.
         *           Used to get tree depth in GetEquipmentTreeDepth().
         * <param> GraphqlResponse currentLayer: Current layer of the equipment tree
         * <param> bool consoleOutput: Whether to print the equipment names to the console
         * <param> int layerNum: Current layer number
         * <returns> Task<int>: Depth of the equipment tree (deepest layer)
         */
        public static async Task<int> IterateEquipmentTree(GraphqlResponse currentLayer, bool consoleOutput = true, int layerNum = 0)
        {
            var maxLayer = layerNum;
            
            // Root layer only
            if (currentLayer == null)
            {
                currentLayer = await GraphqlEquipmentQuery();
            }
            // Normal non-root layer
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
                 
                // Count depth of deepest layer to facilitate accurate GraphQL queries if needed
                if (depth > maxLayer)
                {
                    maxLayer = depth;
                }
            }

            return maxLayer;
        }
        
        
        /* Function GetEquipmentTreeDepth()
         * <summary> Returns the depth of the equipment tree in the zenon runtime
         *           by iterating through the tree and counting the layers.
         *           Used to facilitate accurate GraphQL queries for EquipmentFullQuery().
         * <param> None
         * <returns> Task<int>: Depth of the equipment tree
         */
        public static async Task<int> GetEquipmentTreeDepth()
        {
            var depth = await IterateEquipmentTree(null, false);
            return depth;
        }
        
        
        /* Function EquipmentFullQuery()
         * <summary> Queries the zenon runtime for equipment data via GraphQL.
         *           Not used by connector at the moment, but allows for extending
         *           iTwin connector functionality to include equipment data.
         *           Note: Data is not flattened, so adjust AAS model in connector.
         * <param> None
         * <returns> Task<string>: JSON string containing equipment data
         *
         * Example URL: https://localhost:7262/equipment
         */
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

        
        /* Function AlarmDataQuery()
         * <summary> Queries the zenon runtime for alarm data via GraphQL
         * <param> int seconds: Last n seconds of arriving alarms to be queried
         * <returns> Task<string?>: JSON string containing flat list of alarm data
         *
         * Example URL: https://localhost:7262/alarmData?fromSeconds=5
         */
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
                
                // Flatten the JSON by converting str->obj->flatten->str, so that zenonConnector can properly may values (nested values were causing stealthy issues)
                var responseJSON_obj = JsonConvert.DeserializeObject<Root>(response.Content);
                var Helper = new JsonHelper_Alarms();
                var responseJSON_obj_flat = Helper.Flatten(responseJSON_obj.data.alarmData);
                string responseJSON_string_flat = JsonSerializer.Serialize(responseJSON_obj_flat);
                return Task.FromResult("{\"data\":{\"alarmData\":" + responseJSON_string_flat + "}}");
            }
            catch(Exception e)
            {
                Console.WriteLine(e.ToString());
                return Task.FromResult<string>(null);
            }
        }
        
        
        /* Function VariablesQuery()
         * <summary> Queries the zenon runtime for variable info via GraphQL
         * <param> None
         * <returns> Task<string?>: JSON string containing flat list of variables
         *
         * Example URL: https://localhost:7262/variables
         */
        public static Task<string?> VariablesQuery()
        {
            ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls13;
            var config = new ConfigContainer();
            
            /* Example Query */
            // query{
            //     variables(database: "project_DB", projects: "ZENON11_DEMO"){
            //         variableName,
            //         measuringUnit,
            //         dataType
            //     }
            // }
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
        
        
        /* Function ArchiveQuery()
         * <summary> Queries zenon for archive data of a single archive
         * <param> string archive: (short-)Name of the archive to be queried
         * <param> string startTime: Start of time from which to query
         * <param> string endTime: End time until which to query
         * <returns> Task<string?>: JSON string containing flat list of archive data
         *
         * Example URL: http://localhost:5000/archive?archive=Archive_1&startTime=2021-08-01T00:00:00&endTime=2021-08-02T00:00:00
         */
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
                var Helper = new JsonHelper_Archives();
                var responseJSON_obj_flat = Helper.Flatten(responseJSON_obj.data.archiveData);
                string responseJSON_string_flat = JsonSerializer.Serialize(responseJSON_obj_flat);
                return Task.FromResult("{\"data\":{\"archiveData\":" + responseJSON_string_flat + "}}");
            }
            catch(Exception e)
            {
                Console.WriteLine(e.ToString());
                return Task.FromResult<string>(null);
            }
        }
    }
}