package at.srfg.iasset.connector.isproNG;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Collectors;

public class IsproNGWebHookHandler implements HttpHandler {

    IWebhook webhook;
    public IsproNGWebHookHandler(IWebhook webhook)
    {
        this.webhook = webhook;
    }
    @Override
    public void handle(HttpExchange t) throws IOException {

        InputStreamReader isr =  new InputStreamReader(t.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        String value = br.lines().collect(Collectors.joining());

        this.webhook.Execute(value);

        String response = "OK: RequestBody"+value.toString();
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}