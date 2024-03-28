// https://stackoverflow.com/questions/7895105/deserialize-json-with-c-sharp
namespace testRESTbackend.Classes;



// GraphqlResponse myDeserializedClass = JsonConvert.DeserializeObject<GraphqlResponse>(myJsonResponse);

public class EquipmentGroups
{
    public List<EquipmentGroup> equipmentGroups { get; set; }
}

public class EquipmentGroup
{
    public string guid { get; set; }
    public string name { get; set; }
    public string description { get; set; }
}

public class GraphqlResponse
{
    public EquipmentGroups data { get; set; }
}
