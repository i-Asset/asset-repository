namespace testRESTbackend.Classes;

public class ConfigContainer
{
    public string? AlarmTimeframe { get; set; } = System.Configuration.ConfigurationManager.AppSettings.Get("alarm_timeframe");

    public string? ZenonProject { get; set; } = System.Configuration.ConfigurationManager.AppSettings.Get("zenon_project");

    public string? TokenEndpoint { get; set; } = System.Configuration.ConfigurationManager.AppSettings.Get("token_endpoint");

    public string? ClientId { get; set; } = System.Configuration.ConfigurationManager.AppSettings.Get("client_id");

    public string? ClientSecret { get; set; } = System.Configuration.ConfigurationManager.AppSettings.Get("client_secret");

    public string? GrantType { get; set; } = System.Configuration.ConfigurationManager.AppSettings.Get("grant_type");

    public string? Scope { get; set; } = System.Configuration.ConfigurationManager.AppSettings.Get("scope");

    public string? GraphqlEndpoint { get; set; } = System.Configuration.ConfigurationManager.AppSettings.Get("graphql_endpoint");

    public string? GraphqlDb { get; set; } = System.Configuration.ConfigurationManager.AppSettings.Get("graphql_DB");

    public string? AlarmRefreshInterval { get; set; } = System.Configuration.ConfigurationManager.AppSettings.Get("alarm_refresh_interval");
}