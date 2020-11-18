package com.kodilla.frontend.clinic;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableVaadin
@SpringBootApplication
public class ClinicFrontendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClinicFrontendApplication.class, args);
    }

}
