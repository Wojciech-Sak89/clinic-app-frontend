package com.kodilla.clinic;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableVaadin
@SpringBootApplication(scanBasePackages={"com.kodilla.clinic"})
public class ClinicFrontendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClinicFrontendApplication.class, args);
    }

}
