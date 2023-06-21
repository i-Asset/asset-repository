package at.srfg.iasset.connector.isproNG;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class IsproNGWebHook {
    public IsproNGWebHook(int port,IWebhook webHook) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/IsproConnector", new IsproNGWebHookHandler(webHook));
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
