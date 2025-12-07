package org.example.tms_cargopro.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Transport Management System (TMS) API",
                version = "1.0.0",
                description = "A comprehensive Transport Management System backend API for managing loads, transporters, bids, and bookings",
                contact = @Contact(
                        name = "TMS CargoPro Team",
                        email = "support@cargopro.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local development server"
                )
        }
)
public class OpenApiConfig {
}

