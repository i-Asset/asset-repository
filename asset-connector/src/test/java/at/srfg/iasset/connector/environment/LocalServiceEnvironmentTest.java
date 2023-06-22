package at.srfg.iasset.connector.environment;

import at.srfg.iasset.connector.component.impl.AASFull;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocalServiceEnvironmentTest {

    private String currentStringValue = "123.5";
    private LocalServiceEnvironment serviceEnvironmentUnderTest;

    private static final String repositoryUriString = "http://localhost:18081/"; // not reachable!

    private static final Logger log = LoggerFactory.getLogger(LocalServiceEnvironmentTest.class);

    @BeforeAll
    static void setup(TestInfo testInfo) {
        log.info("executing '{}'", testInfo.getTestClass().map(Class::getName).orElse("unknown class"));
    }

    @BeforeEach
    void init(TestInfo testInfo) throws URISyntaxException {
        log.info("initializing '{}'", testInfo.getTestMethod().map(Method::getName).orElse("unknown method"));
        URI repositoryURI = new URI(repositoryUriString);
        serviceEnvironmentUnderTest = new LocalServiceEnvironment();
        log.info("LocalServiceEnvironment with dummy repositoryURI '{}' created", repositoryUriString);
        loadAASTestData();
        log.info("test data loaded");
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        log.info("'{}' finished", testInfo.getTestMethod().map(Method::getName).orElse("unknown method"));
    }

    @AfterAll
    static void finish(TestInfo testInfo) {
        log.info("'{}' ready", testInfo.getTestClass().map(Class::getName).orElse("unknown class"));
    }
    @DisplayName("Starting and Stopping of Local Service Environment")
    @Test
    void startAndStopEndpoint() {
        final int port = startEndpointOnFreePort();
        assertNotEquals(0, port);

        log.info("local service environment started endpoint with port {}", port);

        // try to open another server on the same port must fail
        IOException thrown = assertThrows(IOException.class, () -> {
            try (ServerSocket s = new ServerSocket(port)) {
                // do nothing
                assertNull(s);
            }
            ;
        }, "IOException was expected");

        Assertions.assertTrue(thrown.getMessage().startsWith("Address already in use"));

        stopEndpoint();
        stopEventProcessor();
    }

    @DisplayName("Basic local data loaded to connector")
    @Test
    void basicLocalData() {
        startEndpointOnFreePort();

        final String id = AASFull.AAS_BELT_INSTANCE.getId();
        assertTrue(serviceEnvironmentUnderTest.getAssetAdministrationShell(id).isPresent());
        assertEquals(AASFull.AAS_BELT_INSTANCE, serviceEnvironmentUnderTest.getAssetAdministrationShell(id).get());

        stopEndpoint();
    }

    @DisplayName("Register AAS with central repository")
    @Test
    @Disabled
    void register() {
        startEndpointOnFreePort();
        final String id = AASFull.AAS_BELT_INSTANCE.getId();
        // expecting false, because this requires a remote running asset repo which
        // takes the registration (not available at "repositoryUriString")
        assertFalse(serviceEnvironmentUnderTest.register(id));

        // no response expected (void)
        serviceEnvironmentUnderTest.unregister(id);

        stopEndpoint();
    }

    @DisplayName("Load data from a central repository")
    @Test
    void initWithRemoteData() {
        startEndpointOnFreePort();
        // expecting exception, because this requires a remote running asset repo which
        // hosts the requested data (not available at "repositoryUriString")
        assertThrows(IllegalArgumentException.class, this::loadAASTestDataWithRemoteModels, "expected IllegalArgumentException!");

        stopEndpoint();
    }

    @DisplayName("Test via REST")
    @Test
    void restTest() {
        int port = startEndpointOnFreePort();
        URI url = URI.create("http://localhost:" + port + "/shells");
        RestTemplate rest = new RestTemplate();
        final ResponseEntity<List> forEntity = rest.getForEntity(url, List.class);
        assertNotNull(forEntity);
        List body = forEntity.getBody();
        assertNotNull(body);
        assertEquals(6, body.size(), "expecting seven preloaded shells");
        log.debug("response: {}", body);
        assertNotNull(body.get(0), "expecting element");
        assertEquals(body.get(0).getClass(), LinkedHashMap.class, "expecting a linked hash map in body");
    }

    @DisplayName("Empty test")
    @Test
    void test() {
        /*
        startEndpointOnFreePort();
        loadAASTestData();
        assertEquals(6, serviceEnvironmentUnderTest.getAllAssetAdministrationShells().size());

        Set<Reference> references = new ReferenceCollector(serviceEnvironmentUnderTest, KeyTypes.BASIC_EVENT_ELEMENT).collect(AASFull.AAS_BELT_INSTANCE);
        assertEquals(1,references.size());

        assertTrue(serviceEnvironmentUnderTest.getAssetAdministrationShell(AASFull.AAS_BELT_INSTANCE.getId()).isPresent());
        log.info("config: {}", serviceEnvironmentUnderTest.getAllAssetAdministrationShells());

         */
    }


    private void stopEventProcessor() {
        serviceEnvironmentUnderTest.getEventProcessor().stopEventProcessing();
    }

    private void stopEndpoint() {
        serviceEnvironmentUnderTest.shutdownEndpoint();
    }

    private int startEndpointOnFreePort() {
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
        serviceEnvironmentUnderTest.setConceptDescription(AASFull.CONCEPT_DESCRIPTION_1.getId(), AASFull.CONCEPT_DESCRIPTION_1);
        serviceEnvironmentUnderTest.setConceptDescription(AASFull.CONCEPT_DESCRIPTION_2.getId(), AASFull.CONCEPT_DESCRIPTION_2);
        serviceEnvironmentUnderTest.setConceptDescription(AASFull.CONCEPT_DESCRIPTION_3.getId(), AASFull.CONCEPT_DESCRIPTION_3);
        serviceEnvironmentUnderTest.setConceptDescription(AASFull.CONCEPT_DESCRIPTION_4.getId(), AASFull.CONCEPT_DESCRIPTION_4);
        serviceEnvironmentUnderTest.setAssetAdministrationShell(AASFull.AAS_BELT_TEMPLATE.getId(), AASFull.AAS_BELT_TEMPLATE);
        serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_BELT_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_PROPERTIES_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_PROPERTIES_TEMPLATE);
        serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_BELT_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_OPERATIONS_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_OPERATIONS_TEMPLATE);

        // belt instance data
        serviceEnvironmentUnderTest.setAssetAdministrationShell(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.AAS_BELT_INSTANCE);
        serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_PROPERTIES_INSTANCE.getId(), AASFull.SUBMODEL_BELT_PROPERTIES_INSTANCE);
        serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_OPERATIONS_INSTANCE.getId(), AASFull.SUBMODEL_BELT_OPERATIONS_INSTANCE);
    }

    private void loadAASTestDataWithRemoteModels() {
        serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_1.getId(), AASFull.SUBMODEL_7.getId(), AASFull.SUBMODEL_7);

        serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_BELT_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_EVENT_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_EVENT_TEMPLATE);

        serviceEnvironmentUnderTest.setSubmodel(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_EVENT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_EVENT_INSTANCE);

    }
}