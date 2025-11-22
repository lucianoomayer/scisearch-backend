package com.lucianoomayer.scisearch;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class SciSearchApplication {

	public static void main(String[] args) {
        String profile = System.getProperty("spring.profiles.active");
        if (profile == null) {
            profile = "dev";
        }

        if ("dev".equals(profile)) {
            Dotenv dotenv = Dotenv.load();
            dotenv.entries().forEach(entry ->
                    System.setProperty(entry.getKey(), entry.getValue())
            );
        }

        SpringApplication app = new SpringApplication(SciSearchApplication.class);
        app.setDefaultProperties(Collections.singletonMap("spring.profiles.active", profile));
        app.run(args);
	}

}
