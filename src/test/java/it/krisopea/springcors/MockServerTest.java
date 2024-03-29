package it.krisopea.springcors;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static it.krisopea.springcors.constants.TestConstants.BASE_URL;
import static it.krisopea.springcors.constants.TestConstants.POST_ENDPOINT;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.verify.VerificationTimes.once;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class MockServerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static MockServerClient mockServer;

    @BeforeAll
    public static void startServer(){
        mockServer = ClientAndServer.startClientAndServer(1080);
    }
    @AfterAll
    public static void stopServer(){
        mockServer.stop();
    }

    @BeforeEach
    public void cleanServer() {
        mockServer.reset();
    }

    @Test
    void testd_Demo_Post_Ok() {
        int port = mockServer.getPort();

        String url = BASE_URL + port + POST_ENDPOINT;

        String requestBody = "{\n" +
                "    \"iuv\":\"123\",\n" +
                "    \"city\":\"MI\",\n" +
                "    \"nation\":\"IT\",\n" +
                "    \"noticeId\":\"1234\"\n" +
                "}";

        String responseBodyExpected = "{\n" +
                "    \"outcome\": \"OK\",\n" +
                "    \"status\": \"ELABORATO\"\n" +
                "}";

        mockServer
                .when(
                        request()
                                .withMethod("POST")
                                .withPath(POST_ENDPOINT)
                                .withBody(requestBody)
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(responseBodyExpected)
                );

        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                requestBody,
                String.class);

        mockServer
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath(POST_ENDPOINT),
                        once()
                        );

        AssertionsForClassTypes.assertThat(response.getBody()).isEqualTo(responseBodyExpected);
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testd_Demo_Post_Ko() {
        int port = mockServer.getPort();

        String url = BASE_URL + port + POST_ENDPOINT;

        String requestBody = "{\n" +
                "    \"iuv\":\"123\",\n" +
                "    \"city\":\"MI\",\n" +
                "    \"nation\":\"IT\",\n" +
                "    \"noticeId\":\"1234\"\n" +
                "}";

        String responseBodyExpected = "{\n" +
                "    \"errorId\": \"a7a9e7ab-da20-419b-97bd-4392e7e5206a\",\n" +
                "    \"timestamp\": \"2024-03-26T13:24:49.532509Z\",\n" +
                "    \"httpStatusCode\": 500,\n" +
                "    \"httpStatusDescription\": \"Internal Server Error\",\n" +
                "    \"appErrorCode\": \"DEMO-0500\",\n" +
                "    \"message\": \"An unexpected error has occurred. Please contact support\"\n" +
                "}";

        mockServer
                .when(
                        request()
                                .withMethod("POST")
                                .withPath(POST_ENDPOINT)
                                .withBody(requestBody)
                )
                .respond(
                        response()
                                .withStatusCode(500)
                                .withBody(responseBodyExpected)
                );

        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                requestBody,
                String.class);

        mockServer
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath(POST_ENDPOINT),
                        once()
                );

        AssertionsForClassTypes.assertThat(response.getBody()).isEqualTo(responseBodyExpected);
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
