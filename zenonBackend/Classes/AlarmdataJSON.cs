public class Root
{
    public Data data { get; set; }
}

public class Data
{
    public List<AlarmDatum> alarmData { get; set; }
}

public class AlarmDatum
{
    public Variable variable { get; set; }
    public string alarmText { get; set; }
    public AlarmClass alarmClass { get; set; }
    public AlarmGroup alarmGroup { get; set; }
    public DateTime timeComes { get; set; }
    public DateTime timeGoes { get; set; }
}

public class AlarmClass
{
    public string name { get; set; }
}

public class AlarmGroup
{
    public string name { get; set; }
}

public class Variable
{
    public string variableName { get; set; }
}




//
// public class Root
// {
//     public Data data { get; set; }
// }
//
// public class Data
// {
//     public List<AlarmDatum> alarmData { get; set; }
// }

public class FlatAlarmData
{
    public string variable { get; set; }
    public string alarmText { get; set; }
    public string alarmClass { get; set; }
    public string alarmGroup { get; set; }
    public DateTime timeComes { get; set; }
    public DateTime timeGoes { get; set; }
}

public class JsonHelper_Alarms
{
    public List<FlatAlarmData> Flatten (List<AlarmDatum> data)
    {
        List<FlatAlarmData> FlatData = new List<FlatAlarmData>();

        foreach (var alarmData in data)
        {
            FlatData.Add(new FlatAlarmData
            {
                variable = alarmData.variable.variableName,
                alarmText = alarmData.alarmText,
                alarmClass = alarmData.alarmClass?.name,
                alarmGroup = alarmData.alarmGroup?.name,
                timeComes = alarmData.timeComes,
                timeGoes = alarmData.timeGoes
            });
        }

        return FlatData;
    }
}