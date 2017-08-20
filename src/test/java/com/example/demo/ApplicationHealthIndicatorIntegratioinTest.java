package com.example.demo;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationHealthIndicatorIntegratioinTest {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private RestTemplate restTemplate;

	//private  server;

//	@Before
//	public void setup() {
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
//		
//	}

	@Test
	public void checkForOverallUp() throws Exception {
		//server = MockRestServiceServer.bindTo(restTemplate).build();
		MockMvc  mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		MockRestServiceServer server = MockRestServiceServer.createServer((RestTemplate) restTemplate);

		server.expect(
		            requestTo("http://localhost:8089"))
		            .andExpect(method(HttpMethod.GET))
		         				.andRespond(new ResponseCreator() {

					@Override
					public ClientHttpResponse createResponse(
							ClientHttpRequest request) throws IOException {
						throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
					}
				});
		   
//		server.expect(requestTo("http://localhost:8089"))
//				.andExpect(method(HttpMethod.GET))
//				.andRespond(new ResponseCreator() {
//
//					@Override
//					public ClientHttpResponse createResponse(
//							ClientHttpRequest request) throws IOException {
//						throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
//					}
//				});

		mockMvc
				.perform(get("/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("UP"))
				.andExpect(jsonPath("$.application.status").value("UP"))
				.andExpect(
						jsonPath("$.application.external-web-service").value(
								"UP"));
		
		server.verify();
	}

	@Test
	public void checkForOverallDown() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		MockRestServiceServer server = MockRestServiceServer.createServer((RestTemplate) restTemplate);
		
		server.expect(
	            requestTo("http://localhost:8089"))
	            .andExpect(method(HttpMethod.GET))
	         				.andRespond(new ResponseCreator() {

				@Override
				public ClientHttpResponse createResponse(
						ClientHttpRequest request) throws IOException {
					throw new ResourceAccessException("ex");
				}
			});
		
//		server.expect(MockMvcRequestBuilders.get("http://localhost:8089"))
//				.andExpect(method(HttpMethod.GET))
//				.andRespond(new ResponseCreator() {
//
//					@Override
//					public ClientHttpResponse createResponse(
//							ClientHttpRequest request) throws IOException {
//						throw new ResourceAccessException("ex");
//					}
//				});

		mockMvc
				.perform(get("/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("UP"))
				.andExpect(jsonPath("$.application.status").value("UP"))
				.andExpect(
						jsonPath("$.application.external-web-service").value(
								"DOWN"));
		server.verify();
	}
}
// https://stackoverflow.com/questions/31819375/inject-mock-into-spring-mockmvc-webapplicationcontext