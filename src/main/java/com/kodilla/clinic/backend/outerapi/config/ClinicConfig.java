package com.kodilla.clinic.backend.outerapi.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ClinicConfig {
    @Value("${clinic.api.endpoint.prod}")
    private String clinicApiEndpoint;

    @Value("workingDays")
    private String workingDays;

    @Value("clinicDoctorsSchedules")
    private String schedules;
}