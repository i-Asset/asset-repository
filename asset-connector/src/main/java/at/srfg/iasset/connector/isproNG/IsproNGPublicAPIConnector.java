package at.srfg.iasset.connector.isproNG;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class IsproNGPublicAPIConnector implements IWebhook{
    String url = "";
    String apiKey = "";
    IsproNGWebHook webHook;

    public IsproNGPublicAPIConnector(String url, String apikey)  throws IOException {
        this.url = url;
        this.apiKey = apikey;

        this.webHook = new IsproNGWebHook(8989,this);
    }

    private Set<Consumer<String>> listeners = new HashSet();

    public void addListener(Consumer<String> listener) {
        listeners.add(listener);
    }

    public void broadcast(String args) {
        listeners.forEach(x -> x.accept(args));
    }

    public String GenerateMaintenanceAlert(IsproNGMaintenanceAlert alert){
       return postObject("StandardInterface/MaintenanceAlert/Save",alert);
    }

    public IsproNGErrorCause[] GetErrorCauses()
    {
        return new IsproNGErrorCause[] {};
    }

    private String postObject(String path, Object data){
        try {
            // Construct manually a JSON object in Java, for testing purposes an object with an object
            ObjectMapper mapper = new ObjectMapper();
            // URL and parameters for the connection, This particulary returns the information passed
            URL ulrObject = new URL(this.url+"/"+path);
            HttpURLConnection httpConnection  = (HttpURLConnection) ulrObject.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("X-api-key",apiKey);

            DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
            wr.write(mapper.writeValueAsString(data).getBytes());
            Integer responseCode = httpConnection.getResponseCode();

            BufferedReader bufferedReader;

            // Creates a reader buffer
            if (responseCode > 199 && responseCode < 300) {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
            }

            // To receive the response
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();

            // Prints the response
            //System.out.println(content.toString());
            return content.toString();

        } catch (Exception e) {
            System.out.println("Error Message");
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }

        return "";
    }

    @Override
    public void Execute(String o) {
        System.out.println("Webhook received:"+o);
        broadcast(o);
    }
}
