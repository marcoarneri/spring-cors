# MockServer

## Passaggi

Segui questi passaggi per aggiungere MockServer al tuo progetto Spring Boot:

### 1. Aggiunta delle Dipendenze Necessarie

- Aggiungi le seguenti dipendenze al tuo file pom.xml per includere MockServer nel tuo progetto:

```xml
<dependencies>
    <dependency>
        <groupId>org.mock-server</groupId>
        <artifactId>mockserver-client-java</artifactId>
        <version>5.15.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mock-server</groupId>
        <artifactId>mockserver-netty</artifactId>
        <version>5.15.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```
***
### 2. Configurazione del MockServer
- Crea una classe di configurazione per il tuo MockServer. Puoi avviare e fermare il server in modo pulito utilizzando le annotazioni `@BeforeAll`, `@AfterAll` e `@BeforeEach`, esempio ([MockServerTest.java](src%2Ftest%2Fjava%2Feu%2Ftasgroup%2Fspringbootguide%2FMockServerTest.java)).
```java
public class MockServerConfig {
    @BeforeAll
    public static void startServer() {
        mockServer = ClientAndServer.startClientAndServer(1080);
    }

    @AfterAll
    public static void stopServer() {
        mockServer.stop();
    }
    
    @BeforeEach
    public void cleanServer() {
        mockServerClient.reset();
    }
}
```
***
### 3. Creazione delle Expectation
Le "expectation" (aspettative) in MockServer definiscono il comportamento che ci si aspetta dal server mockato quando riceve una determinata richiesta HTTP. In pratica, le expectation consentono di specificare quali risposte il server mockato dovrebbe restituire in risposta a determinate richieste.

- Definisci le aspettative del server utilizzando il client MockServer come nell'esempio:
```java
public class MockExpectation {

    public MockServerClient mockServerClient;

    public MockExpectation(MockServerClient mockServerClient) {
        this.mockServerClient = mockServerClient;
    }

    public void setUpExpectation(String httpMethod, String path, Integer status) {
        mockServerClient
                .when(
                        HttpRequest.request()
                                .withMethod(httpMethod)
                                .withPath(path)
                )
                .respond(
                        HttpResponse.response()
                                .withStatusCode(status)
                                .withBody(mockResponse)
                );
    }
}
```
***
### 4. Creazione dei Test
-  Crea i test che interagiscono con il server mockato, esempio ([MockServerTest.java](src%2Ftest%2Fjava%2Feu%2Ftasgroup%2Fspringbootguide%2FMockServerTest.java)).

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestMockExpectation {
    @Test
    public void DemoMock_OK() {
        MockExpectation expectation = new MockExpectation(mockServerClient);
        expectation.setUpExpectation("POST", "/endPoint", 200);

        String response = restTemplate.postForObject(MOCK_URL + "/endPoint", null, String.class);

        assertThat(response).isNotNull().contains(ExpectationUtil.setUpResponse("200"));
    }
}
```











