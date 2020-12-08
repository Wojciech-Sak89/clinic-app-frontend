package com.kodilla.clinic.backend.outerapi.client;

import com.kodilla.clinic.backend.enums.Gender;
import com.kodilla.clinic.backend.outerapi.config.ClinicConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@Service
public class ClinicURIs {
    @Autowired
    private ClinicConfig clinicConfig;

    public URI getSymptomsUri() {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getSymptoms())
                .build().encode().toUri();
    }

    public URI getRecommendationsUri(Integer birthYear, Gender gender, int[] symptomsIdsInts) {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getRecommendations())
                .queryParam("birthYear", birthYear)
                .queryParam("gender", gender)
                .query("symptoms=" + Arrays.toString(symptomsIdsInts))
                .build().encode().toUri();
    }

    public URI getWorkingDaysUri() {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getWorkingDays())
                .build().encode().toUri();
    }

    public URI deleteWorkingDayUri(Integer id) {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getWorkingDays() + "/" + id)
                .build().encode().toUri();
    }

    public URI getSchedulesUri() {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getSchedules())
                .build().encode().toUri();
    }

    public URI getEmergencyHoursUri() {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getEmergencyHours())
                .build().encode().toUri();
    }

    public URI deleteEmergencyHourUri(Integer id) {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getEmergencyHours() + "/" + id)
                .build().encode().toUri();
    }

    public URI getDoctorsUri() {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getDoctors())
                .build().encode().toUri();
    }

    public URI deleteDoctorUri(Integer id) {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getDoctors() + "/" + id)
                .build().encode().toUri();
    }

    public URI getPatientsUri() {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getPatients())
                .build().encode().toUri();
    }

    public URI deletePatientUri(Integer id) {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getPatients() + "/" + id)
                .build().encode().toUri();
    }

    public URI getEvaluationsUri() {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getStaffEvaluations())
                .build().encode().toUri();
    }

    public URI deleteEvaluationUri(Integer id) {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getStaffEvaluations() + "/" + id)
                .build().encode().toUri();
    }

    public URI getAppointmentsUri() {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getAppointments())
                .build().encode().toUri();
    }

    public URI deleteAppointmentUri(Integer appointment_id) {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getAppointments() + "/" + appointment_id)
                .build().encode().toUri();
    }
}
