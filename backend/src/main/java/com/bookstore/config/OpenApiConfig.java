package com.bookstore.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080/api");
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setName("Bookstore API Support");
        contact.setEmail("support@bookstore.com");
        contact.setUrl("https://www.bookstore.com");

        License license = new License().name("MIT License").url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Bookstore API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints for the Bookstore application.")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
} 