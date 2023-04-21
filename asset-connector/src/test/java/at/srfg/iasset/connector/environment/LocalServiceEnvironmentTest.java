package at.srfg.iasset.connector.environment;

import at.srfg.iasset.connector.component.impl.AASFull;
import at.srfg.iasset.repository.model.helper.visitor.ReferenceCollector;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Reference;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LocalServiceEnvironmentTest {

    private String currentStringValue = "123.5";
    private LocalServiceEnvironment serviceEnvironmentUnderTest;

    private static final String repositoryUriString = "http://localhost:18081/"; // not reachable!

    private static final Logger log = LoggerFactory.getLogger(LocalServiceEnvironmentTest.class);

    @BeforeAll
    static void setup() {
        log.info("@BeforeAll currently empty");
    }

    @BeforeEach
    void init() throws URISyntaxException {
        log.info("create Local Service Environment with repositoryURI '{}' ", repositoryUriString);
        URI repositoryURI = new URI(repositoryUriString);
        serviceEnvironmentUnderTest = new LocalServiceEnvironment(repositoryURI);
        loadAASTestData();
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

    @DisplayName("Register AAS with central repository")
    @Disabled
    @Test
    void register() {
        startEndpoint();
        assertTrue(serviceEnvironmentUnderTest.register("https://acplt.org/Test_AssetAdministrationShell"));
        //
        final String id = AASFull.AAS_BELT_INSTANCE.getId();
        //serviceEnvironmentUnderTest.unregister(id);
        //assertTrue(serviceEnvironmentUnderTest.register(id));

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

    private void loadAASTestData() {
        // init with test data!
        serviceEnvironmentUnderTest.setAssetAdministrationShell(AASFull.AAS_1.getId(), AASFull.AAS_1);
        serviceEnvironmentUnderTest.setAssetAdministrationShell(AASFull.AAS_2.getId(), AASFull.AAS_2);
        serviceEnvironmentUnderTest.setAssetAdministrationShell(AASFull.AAS_3.getId(), AASFull.AAS_3);
        serviceEnvironmentUnderTest.setAssetAdministrationShell(AASFull.AAS_4.getId(), AASFull.AAS_4);
        serviceEnvironmentUnderTest.setAssetAdministrationShell(AASFull.AAS_4.getId(), AASFull.AAS_4);
        serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_1.getId(), AASFull.SUBMODEL_1.getId(), AASFull.SUBMODEL_1);
        serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_1.getId(), AASFull.SUBMODEL_2.getId(), AASFull.SUBMODEL_2);
        serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_1.getId(), AASFull.SUBMODEL_3.getId(), AASFull.SUBMODEL_3);
        serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_1.getId(), AASFull.SUBMODEL_4.getId(), AASFull.SUBMODEL_4);
        serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_1.getId(), AASFull.SUBMODEL_5.getId(), AASFull.SUBMODEL_5);
        serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_1.getId(), AASFull.SUBMODEL_6.getId(), AASFull.SUBMODEL_6);
        //serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_1.getId(), AASFull.SUBMODEL_7.getId(), AASFull.SUBMODEL_7);
        serviceEnvironmentUnderTest.setConceptDescription(AASFull.CONCEPT_DESCRIPTION_1.getId(), AASFull.CONCEPT_DESCRIPTION_1);
        serviceEnvironmentUnderTest.setConceptDescription(AASFull.CONCEPT_DESCRIPTION_2.getId(), AASFull.CONCEPT_DESCRIPTION_2);
        serviceEnvironmentUnderTest.setConceptDescription(AASFull.CONCEPT_DESCRIPTION_3.getId(), AASFull.CONCEPT_DESCRIPTION_3);
        serviceEnvironmentUnderTest.setConceptDescription(AASFull.CONCEPT_DESCRIPTION_4.getId(), AASFull.CONCEPT_DESCRIPTION_4);
        //
        serviceEnvironmentUnderTest.setAssetAdministrationShell(AASFull.AAS_BELT_TEMPLATE.getId(), AASFull.AAS_BELT_TEMPLATE);
        serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_BELT_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_PROPERTIES_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_PROPERTIES_TEMPLATE);
        //serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_BELT_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_EVENT_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_EVENT_TEMPLATE);
        serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_BELT_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_OPERATIONS_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_OPERATIONS_TEMPLATE);
        // belt instance data
        serviceEnvironmentUnderTest.setAssetAdministrationShell(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.AAS_BELT_INSTANCE);
        serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_PROPERTIES_INSTANCE.getId(), AASFull.SUBMODEL_BELT_PROPERTIES_INSTANCE);
        //serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_EVENT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_EVENT_INSTANCE);
        serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_OPERATIONS_INSTANCE.getId(), AASFull.SUBMODEL_BELT_OPERATIONS_INSTANCE);
    }
}