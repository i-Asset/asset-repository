// Root myDeserializedClass = JsonConvert.DeserializeObject<Root>(myJsonResponse);
public class ArchiveData
{
    public List<ArchiveDatum> archiveData { get; set; }
}

public class ArchiveRoot
{
    public ArchiveData data { get; set; }
}

public class ArchiveDatum
{
    public DateTime timestamp { get; set; }
    public string stringValue { get; set; }
    public double numericValue { get; set; }
    public string calculation { get; set; }
    public ArchiveVariable variable { get; set; }
}


public class ArchiveVariable
{
    public string variableName { get; set; }
    public string dataType { get; set; }
}





public class FlatArchiveData
{
    public string variableName { get; set; }
    public string variableDataType { get; set; }
    public string stringValue { get; set; }
    public double numericValue { get; set; }
    public string calculation { get; set; }
    public DateTime timestamp { get; set; }
}

public class JsonHelper_Archives
{
    public List<FlatArchiveData> Flatten (List<ArchiveDatum> data)
    {
        List<FlatArchiveData> FlatData = new List<FlatArchiveData>();

        foreach (var archiveData in data)
        {
            FlatData.Add(new FlatArchiveData
            {
                variableName = archiveData.variable.variableName,
                variableDataType = archiveData.variable.dataType,
                stringValue = archiveData.stringValue,
                numericValue = archiveData.numericValue,
                calculation = archiveData.calculation,
                timestamp = archiveData.timestamp
            });
        }

        return FlatData;
    }
}