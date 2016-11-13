package com.example.presentation;


import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { "logging.level.org.springframework=ERROR" })
@ActiveProfiles({ "in_memory" })
abstract class RESTTests {

	protected final RestOperations ops = new RestTemplate();
	
	@LocalServerPort
	protected int serverPort;
}
