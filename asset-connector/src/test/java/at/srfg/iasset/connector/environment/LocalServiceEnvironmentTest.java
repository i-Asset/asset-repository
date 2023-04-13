package at.srfg.iasset.connector.environment;

import at.srfg.iasset.connector.component.impl.AASFull;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class LocalServiceEnvironmentTest {

    private String currentStringValue = "123.5";
    private LocalServiceEnvironment serviceEnvironmentUnderTest;

    private static final String repositoryUriString = "http://localhost:18081/";

    private static final Logger log = LoggerFactory.getLogger(LocalServiceEnvironmentTest.class);

    @BeforeAll
    static void setup() {
        log.info("@BeforeAll currently empty");
    }

    @BeforeEach
    void init() throws URISyntaxException {
        log.info("create Local Service Environment");
        URI repositoryURI = new URI(repositoryUriString);
        serviceEnvironmentUnderTest = new LocalServiceEnvironment(repositoryURI);
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("Starting and Stopping of Local Service Environment")
    @Test
    void startAndStopEndpoint() {
        final int port = startEndpoint();
        assertNotEquals(0, port);

        log.info("local service environment started endpoint with port {}", port);

        // try to open another server on the same port must fail
        IOException thrown = assertThrows(IOException.class, () -> {
            try (ServerSocket s = new ServerSocket(port)) {
                // do nothing
                assertNull(s);
            } ;
        }, "IOException was expected");

        Assertions.assertTrue(thrown.getMessage().startsWith("Address already in use"));

        stopEndpoint();
        stopEventProcessor();
    }

    @DisplayName("Register AAS in Local Service Environment")
    @Test
    void register() {
        startEndpoint();
        serviceEnvironmentUnderTest.register("https://acplt.org/Test_AssetAdministrationShell");
        //
        final String id = AASFull.AAS_BELT_INSTANCE.getId();
        serviceEnvironmentUnderTest.register(id);

        assertTrue(serviceEnvironmentUnderTest.getAssetAdministrationShell(id).isPresent());
        assertEquals(AASFull.AAS_BELT_INSTANCE, serviceEnvironmentUnderTest.getAssetAdministrationShell(id).get());
        stopEndpoint();
    }

    private void stopEventProcessor() {
        serviceEnvironmentUnderTest.getEventProcessor().stopEventProcessing();
    }

    private void stopEndpoint() {
        serviceEnvironmentUnderTest.shutdownEndpoint();
    }

    private int startEndpoint() {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            assertNotNull(serverSocket);
            final int localPort = serverSocket.getLocalPort();
            assertNotEquals(0, localPort);
            serverSocket.close();
            serviceEnvironmentUnderTest.startEndpoint(localPort);
            return localPort;
        } catch (IOException e) {
            fail("Port is not available");
            return 0;
        }
    }

}