package com.abnamro.T24.clinet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class T24RestclientApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(T24RestclientApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
	
		
	}
	
	
	
	@Bean
    public RestTemplate geRestTemplate() {
        return new RestTemplate();
    }
}
