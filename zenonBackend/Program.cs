using Microsoft.AspNetCore.Mvc;
using Microsoft.OpenApi.Models;
using testRESTbackend;

var builder = WebApplication.CreateBuilder(args);
    
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddCors(options =>
{
    options.AddPolicy(name: "AngularFrontend",
        policy  =>
        {
            policy.WithOrigins("http://localhost:4200")
                .AllowAnyHeader()
                .AllowAnyMethod();
        });
});
builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new OpenApiInfo { Title = "PizzaStore API", Description = "Making the Pizzas you love", Version = "v1" });
});
    
var app = builder.Build();
    
app.UseCors("AngularFrontend");
app.UseSwagger();
app.UseSwaggerUI(c =>
{
    c.SwaggerEndpoint("/swagger/v1/swagger.json", "zenon AAS WebTool API V1");
});
    

var token = Backend.GetEligibilityToken();
var tokenStr = Backend.GetJsonParameter(token, "access_token");
var cookie1 = $"access_token={tokenStr}; Path=/; HttpOnly;";
var cookie2 = $"{tokenStr};path=/;secure;HttpOnly";


app.MapGet("/", () => "Hello World!");
app.MapGet("/auth_token", () => tokenStr);

app.MapGet("/auth_cookie1", (HttpContext context) =>
{
    context.Response.Headers.Append("Cookie", cookie1);
});

app.MapGet("/auth_cookie2", (HttpContext context) =>
{
    context.Response.Cookies.Append("access_token", cookie2);
});

//app.MapGet("/equipmentquery_01", () => Backend.GraphqlEquipmentQuery()); 
app.MapGet("/tree", () => Backend.IterateEquipmentTree(null)); // debugging purposes - prints to console
app.MapGet("/equipment", () => Backend.EquipmentFullQuery()); 
app.MapGet("/alarmdata", (int fromSeconds) => Backend.AlarmDataQuery(fromSeconds));
app.MapGet("/variables", () => Backend.VariablesQuery());
app.MapGet("/archive", (string archive, string startTime, string endTime) => Backend.ArchiveQuery(archive, startTime, endTime));



app.Run();