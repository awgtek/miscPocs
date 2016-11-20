package com.awgtek.mvcrestclient;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.awgtek.memoizer.Memoizer;
import com.awgtek.memoizerestcall.Params;

@Configuration
@EnableWebMvc
@ComponentScan("com.awgtek.memoizer")
public class WebConfig extends WebMvcConfigurerAdapter {
    
    @Mock
    JerseyClient jerseyClient;

	 public WebConfig() {
	        MockitoAnnotations.initMocks(this); //This is a key
	    }
	
	@Bean
    public ViewResolver internalResourceViewResolver() {
        final InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setViewClass(JstlView.class);
        bean.setPrefix("/WEB-INF/jsp/");
        bean.setSuffix(".jsp");
        bean.setOrder(2);
        return bean;
    }
	
	@Bean
	public JerseyClient getJerseyClient() {
		return jerseyClient;
	}
	@Bean
	public ClientController clientController() {
		return new ClientController();
	}
	
//	@Scope(value="request", proxyMode =ScopedProxyMode.TARGET_CLASS)
//	@Bean
//	public Memoizer<Params, String> memoizer() {
//		return new Memoizer<>();
//	}
}
