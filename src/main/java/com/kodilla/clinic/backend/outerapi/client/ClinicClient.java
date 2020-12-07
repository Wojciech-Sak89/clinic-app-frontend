package com.kodilla.clinic.backend.outerapi.client;

import com.kodilla.clinic.backend.enums.Gender;
import com.kodilla.clinic.backend.outerapi.config.ClinicConfig;
import com.kodilla.clinic.backend.outerapi.dtos.AppointmentDto;
import com.kodilla.clinic.backend.outerapi.dtos.DoctorDto;
import com.kodilla.clinic.backend.outerapi.dtos.PatientDto;
import com.kodilla.clinic.backend.outerapi.dtos.StaffEvaluationDto;
import com.kodilla.clinic.backend.outerapi.dtos.medic.RecommendationDto;
import com.kodilla.clinic.backend.outerapi.dtos.medic.SymptomDto;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.ClinicDoctorScheduleDto;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.EmergencyHourDto;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.WorkingDayDto;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class ClinicClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClinicClient.class);

    @Autowired
    private ClinicConfig clinicConfig;

    @Autowired
    private RestTemplate restTemplate;

    //WORKING DAYS
    public List<WorkingDayDto> getWorkingDays() {
        URI url = getWorkingDaysUri();

        try {
            Optional<WorkingDayDto[]> workingDaysResponse = Optional.ofNullable(restTemplate.getForObject(url, WorkingDayDto[].class));
            System.out.println(url);
            return Arrays.asList(workingDaysResponse.orElse(new WorkingDayDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public void saveWorkingDay(WorkingDayDto workingDayDto) {
        URI url = getWorkingDaysUri();
        restTemplate.postForObject(url, workingDayDto, WorkingDayDto.class);
    }

    public void deleteWorkingDayById(Integer id) {
        URI url = deleteWorkingDayUri(id);
        System.out.println(url);

        try {
            restTemplate.delete(url);
            System.out.println(url);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    //EMERGENCY HOURS
    public List<EmergencyHourDto> getEmergencyHours() {
        URI url = getEmergencyHoursUri();

        try {
            Optional<EmergencyHourDto[]> emergencyHoursResponse = Optional.ofNullable(restTemplate.getForObject(url, EmergencyHourDto[].class));
            System.out.println(url);
            return Arrays.asList(emergencyHoursResponse.orElse(new EmergencyHourDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public void saveEmergencyHour(EmergencyHourDto emergencyHourDto) {
        URI url = getEmergencyHoursUri();
        restTemplate.postForObject(url, emergencyHourDto, EmergencyHourDto.class);
    }

    public void updateEmergencyHour(EmergencyHourDto emergencyHourDto) {
        URI url = getEmergencyHoursUri();
        restTemplate.put(url, emergencyHourDto);
    }

    public void deleteEmergencyHourById(Integer id) {
        URI url = deleteEmergencyHourUri(id);
        System.out.println(url);

        try {
            restTemplate.delete(url);
            System.out.println(url);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


    //SCHEDULES
    public List<ClinicDoctorScheduleDto> getSchedules() {
        URI url = getSchedulesUri();
        System.out.println(url);

        try {
            Optional<ClinicDoctorScheduleDto[]> schedulesResponse =
                    Optional.ofNullable(restTemplate.getForObject(url, ClinicDoctorScheduleDto[].class));
            System.out.println(url);
            return Arrays.asList(schedulesResponse.orElse(new ClinicDoctorScheduleDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    //DOCTORS
    public void saveDoctor(DoctorDto doctorDto) {
        URI url = getDoctorsUri();
        restTemplate.postForObject(url, doctorDto, DoctorDto.class);
    }

    public void deleteDoctorById(Integer doctor_id) {
        URI url = deleteDoctorUri(doctor_id);
        System.out.println("Delete doctor: " + url);

        try {
            restTemplate.delete(url);
            System.out.println("Delete doctor: " + url);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<DoctorDto> getDoctors() {
        URI url = getDoctorsUri();

        try {
            Optional<DoctorDto[]> doctorsResponse = Optional.ofNullable(restTemplate.getForObject(url, DoctorDto[].class));
            System.out.println("Get doctors: " + url);
            return Arrays.asList(doctorsResponse.orElse(new DoctorDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    //PATIENTS
    public void savePatient(PatientDto patientDto) {
        System.out.println(patientDto);
        URI url = getPatientsUri();
        restTemplate.postForObject(url, patientDto, PatientDto.class);
    }

    public void deletePatientById(Integer patient_id) {
        URI url = deletePatientUri(patient_id);
        System.out.println("Delete aptient: " + url);

        try {
            restTemplate.delete(url);
            System.out.println("Delete patient: " + url);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<PatientDto> getPatients() {
        URI url = getPatientsUri();

        try {
            Optional<PatientDto[]> patientsResponse = Optional.ofNullable(restTemplate.getForObject(url, PatientDto[].class));
            System.out.println("Get patients: " + url);
            return Arrays.asList(patientsResponse.orElse(new PatientDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    //MEDIC API
    public List<SymptomDto> getSymptoms() {
        URI url = getSymptomsUri();

        try {
            Optional<SymptomDto[]> symptomsResponse = Optional.ofNullable(restTemplate.getForObject(url, SymptomDto[].class));
            System.out.println("Get symptoms: " + url);
            return Arrays.asList(symptomsResponse.orElse(new SymptomDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<RecommendationDto> getRecommendations(Integer birthYear, Gender gender, int[] symptomsIdsInts) {
        URI url = getRecommendationsUri(birthYear, gender, symptomsIdsInts);
        System.out.println("getRecommendationsUri(): " + url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<Integer> integersSymptoms = new ArrayList<>(Arrays.asList((ArrayUtils.toObject(symptomsIdsInts))));

        HttpEntity<Object> requestEntity = new HttpEntity<>(integersSymptoms, headers);

        try {
            Optional<RecommendationDto[]> recommendationsResponse =
                    Optional.ofNullable(restTemplate.postForObject(url, requestEntity, RecommendationDto[].class));
            System.out.println("Get recommendations: " + url);
            return Arrays.asList(recommendationsResponse.orElse(new RecommendationDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    //STAFF EVALUATIONS
    public List<StaffEvaluationDto> getStaffEvaluations() {
        URI url = getEvaluationsUri();

        try {
            Optional<StaffEvaluationDto[]> evaluationsResponse = Optional.ofNullable(restTemplate.getForObject(url, StaffEvaluationDto[].class));
            System.out.println("Get evaluations: " + url);
            return Arrays.asList(evaluationsResponse.orElse(new StaffEvaluationDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public void saveStaffEvaluation(StaffEvaluationDto staffEvaluationDto) {
        System.out.println(staffEvaluationDto);
        URI url = getEvaluationsUri();
        restTemplate.postForObject(url, staffEvaluationDto, PatientDto.class);
    }

    public void deleteStaffEvaluationById(Integer evaluation_id) {
        URI url = deleteEvaluationUri(evaluation_id);
        System.out.println("Delete evaluation: " + url);

        try {
            restTemplate.delete(url);
            System.out.println("Delete evaluation: " + url);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    //APPOINTMENTS
    public List<AppointmentDto> getAppointments() {
        URI url = getAppointmentsUri();

        try {
            Optional<AppointmentDto[]> evaluationsResponse = Optional.ofNullable(restTemplate.getForObject(url, AppointmentDto[].class));
            System.out.println("Get appointments: " + url);
            return Arrays.asList(evaluationsResponse.orElse(new AppointmentDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public void saveAppointment(AppointmentDto appointmentDto) {
        System.out.println(appointmentDto);
        URI url = getAppointmentsUri();
        restTemplate.postForObject(url, appointmentDto, AppointmentDto.class);
    }

    public void deleteAppointmentById(Integer appointment_id) {
        URI url = deleteAppointmentUri(appointment_id);
        System.out.println("Delete appointment: " + url);

        try {
            restTemplate.delete(url);
            System.out.println("Delete appointment: " + url);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


    //\\ URIs //\\
    private URI getSymptomsUri() {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getSymptoms())
                .build().encode().toUri();
    }

    private URI getRecommendationsUri(Integer birthYear, Gender gender, int[] symptomsIdsInts) {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getRecommendations())
                .queryParam("birthYear", birthYear)
                .queryParam("gender", gender)
                .query("symptoms=" + Arrays.toString(symptomsIdsInts))
                .build().encode().toUri();
    }

    private URI getWorkingDaysUri() {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getWorkingDays())
                .build().encode().toUri();
    }

    private URI deleteWorkingDayUri(Integer id) {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getWorkingDays() + "/" + id)
                .build().encode().toUri();
    }

    private URI getSchedulesUri() {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getSchedules())
                .build().encode().toUri();
    }

    private URI getEmergencyHoursUri() {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getEmergencyHours())
                .build().encode().toUri();
    }

    private URI deleteEmergencyHourUri(Integer id) {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getEmergencyHours() + "/" + id)
                .build().encode().toUri();
    }

    private URI getDoctorsUri() {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getDoctors())
                .build().encode().toUri();
    }

    private URI deleteDoctorUri(Integer id) {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getDoctors() + "/" + id)
                .build().encode().toUri();
    }

    private URI getPatientsUri() {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getPatients())
                .build().encode().toUri();
    }

    private URI deletePatientUri(Integer id) {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getPatients() + "/" + id)
                .build().encode().toUri();
    }

    private URI getEvaluationsUri() {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getStaffEvaluations())
                .build().encode().toUri();
    }

    private URI deleteEvaluationUri(Integer id) {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getStaffEvaluations() + "/" + id)
                .build().encode().toUri();
    }

    private URI getAppointmentsUri() {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getAppointments())
                .build().encode().toUri();
    }

    private URI deleteAppointmentUri(Integer appointment_id) {
        return UriComponentsBuilder.fromHttpUrl(clinicConfig.getClinicApiEndpoint() + clinicConfig.getAppointments() + "/" + appointment_id)
                .build().encode().toUri();
    }
}
