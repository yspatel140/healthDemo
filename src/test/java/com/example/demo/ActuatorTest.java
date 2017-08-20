package com.example.demo;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
public class ActuatorTest {
@Configuration
static class Config
{
    @Bean
    public ApplicationHealthIndicator dbHealthIndicator()
    {
        return Mockito.mock(ApplicationHealthIndicator.class);
    }
}

@Autowired
private MockMvc mvc;

@Autowired
private ApplicationHealthIndicator dbHealthIndicator;

@Test
public void healthcheck_Up() throws Exception
{
    when(dbHealthIndicator.health()).thenReturn(Health.status(Status.UP).build());

    mvc.perform(get("/health.json"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status").value("UP"));

}

@Test
public void healthcheck_Down() throws Exception
{
    when(dbHealthIndicator.health()).thenReturn(Health.status(Status.DOWN).build());

    mvc.perform(get("/health.json"))
            .andExpect(status().isServiceUnavailable())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.status").value("DOWN"));

}}

//	@Test
//	public void healthcheck_Down() throws Exception {
//		when(dbHealthIndicator.health()).thenReturn(
//				Health.status(Status.DOWN).build());
//
//		mvc.perform(get("/health.json"))
//				.andExpect(status().isServiceUnavailable())
//				.andExpect(
//						content().contentType(MediaType.APPLICATION_JSON_UTF8))
//				.andExpect(jsonPath("$.status").value("DOWN"));
//
//	}
	
//	@Test
//	public void healthcheck_Up() throws Exception {
//		ApplicationHealthIndicator applicationHealthIndicator=new ApplicationHealthIndicator();
//		
//		   MockitoAnnotations.initMocks(this);
//		   MockMvc	   mvc = MockMvcBuilders
//	                .standaloneSetup(applicationHealthIndicator)
//	                .build();
//	        
//		 RestTemplate restTemplate = new RestTemplate();
//	        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
//	        
//		   server.expect(requestTo("http://localhost:8089")).andExpect(method(HttpMethod.GET))
//	         .andRespond(withServerError());
//		   
////		when(dbHealthIndicator.health()).thenReturn(
////				Health.status(Status.UP).build());
//
//		mvc.perform(get("/health.json"))
//				.andExpect(status().isNotFound())
////				.andExpect(
////						content().contentType(MediaType.APPLICATION_JSON_UTF8))
//				.andExpect(jsonPath("$.status").value("UP"));
//
//	}
//}