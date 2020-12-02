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

    @Value("emergencyHours")
    private String emergencyHours;

    @Value("doctors")
    private String doctors;

    @Value("patients")
    private String patients;

    @Value("staffEvaluations")
    private String staffEvaluations;

    @Value("medic/symptoms")
    private String symptoms;

    @Value("medic/recommendations")
    private String recommendations;

    @Value("clinicDoctorsSchedules")
    private String schedules;

    @Value("appointments")
    private String appointments;

}