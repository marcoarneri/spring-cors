package it.krisopea.springcors;

import it.krisopea.springcors.controller.advice.model.ApiErrorResponse;
import it.krisopea.springcors.controller.model.DemoRequest;
import it.krisopea.springcors.controller.model.DemoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static it.krisopea.springcors.constants.TestConstants.BASE_URL;
import static it.krisopea.springcors.constants.TestConstants.POST_ENDPOINT;
import static it.krisopea.springcors.util.GeneratedParams.generateIuv;
import static it.krisopea.springcors.util.GeneratedParams.generateNoticeId;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringCorsApplicationTests {

	@Test
	void contextLoads() {
	}
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void insertsOk() {
		String iuv = generateIuv();
		String noticeId = generateNoticeId();

		String url = BASE_URL + port + POST_ENDPOINT;

		DemoRequest demoRequest = new DemoRequest();
		demoRequest.setIuv(iuv);
		demoRequest.setCity("MI");
		demoRequest.setNation("IT");
		demoRequest.setNoticeId(noticeId);

		ResponseEntity<DemoResponse> response = restTemplate.postForEntity(
				url,
				demoRequest,
				DemoResponse.class);

		assertThat( response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getOutcome()).isEqualTo("OK");
		assertThat(response.getBody().getStatus()).isEqualTo("ELABORATO");
	}

	@Test
	public void insertsKo() {
		String iuv = generateIuv();
		String noticeId = generateNoticeId();

		String url = BASE_URL + port + POST_ENDPOINT;

		DemoRequest demoRequest = new DemoRequest();
		demoRequest.setIuv(iuv);
		demoRequest.setCity("MI");
		demoRequest.setNation("IT");
		demoRequest.setNoticeId(noticeId);

		ResponseEntity<DemoResponse> response = restTemplate.postForEntity(
				url,
				demoRequest,
				DemoResponse.class);

		assertThat( response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getOutcome()).isEqualTo("OK");
		assertThat(response.getBody().getStatus()).isEqualTo("ELABORATO");

		DemoRequest demoRequest2 = new DemoRequest();
		demoRequest2.setIuv(iuv);
		demoRequest2.setCity("MI");
		demoRequest2.setNation("IT");
		demoRequest2.setNoticeId(noticeId);

		ResponseEntity<ApiErrorResponse> response2 = restTemplate.postForEntity(
				url,
				demoRequest2,
				ApiErrorResponse.class);

		assertThat(response2.getBody().getAppErrorCode()).isEqualTo("DEMO-1001");
		assertThat(response2.getBody().getMessage()).isEqualTo( "Iuv duplicate");
	}
}
