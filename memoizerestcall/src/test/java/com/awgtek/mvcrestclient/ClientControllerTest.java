package com.awgtek.mvcrestclient;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class ClientControllerTest {
	 
    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;
//    @Autowired 
//    MockHttpServletRequest request;
    
    @Autowired
    JerseyClient jerseyClient;
    
    @Autowired
    private ClientController clientController;
 
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

	@Test
	public void testNotMemoized() throws Exception {
		mockMvc.perform(get("/mo/notmemoized")).
		   andExpect(view().name("client"));
		verify(jerseyClient, times(3)).serverData(anyString(), eq("mo"));
	}

	@Test
	public void testClientMemoized() throws Exception {
		//MockHttpSession mocksession = new MockHttpSession();
		when(jerseyClient.serverData(anyString(), eq("mo"))).thenReturn("{\"ok\":\"ok\"}");

		mockMvc.perform(get("/mo/memoized"))//.session(mocksession)
		
//		.with(new RequestPostProcessor() { 
//		    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
//		       request.setRemoteAddr("12345"); 
//		       return request;
//		    }}))
		.andExpect(view().name("client"));
		
		verify(jerseyClient, times(1)).serverData(anyString(), eq("mo"));
	}

}
