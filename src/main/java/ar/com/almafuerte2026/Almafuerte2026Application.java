package ar.com.almafuerte2026;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Almafuerte2026Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Almafuerte2026Application.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(Almafuerte2026Application.class, args);
	}
}

