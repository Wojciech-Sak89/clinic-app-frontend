package com.kodilla.clinic.backend.outerapi.client;

import com.kodilla.clinic.backend.outerapi.config.ClinicConfig;
import com.kodilla.clinic.backend.outerapi.dtos.DoctorDto;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.ClinicDoctorScheduleDto;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.EmergencyHourDto;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.WorkingDayDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        System.out.println(doctorDto);
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


    //\\ URIs //\\
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



//    public CreatedTrelloCardDto createNewCard(TrelloCardDto trelloCardDto) {
//
//        URI url = UriComponentsBuilder.fromHttpUrl(trelloConfig.getTrelloApiEndpoint() + "/cards")
//                .queryParam("key", trelloConfig.getTrelloAppKey())
//                .queryParam("token", trelloConfig.getTrelloToken())
//                .queryParam("name", trelloCardDto.getName())
//                .queryParam("desc", trelloCardDto.getDescription())
//                .queryParam("pos", trelloCardDto.getPos())
//                .queryParam("idList", trelloCardDto.getListId()).build().encode().toUri();
//
//        return restTemplate.postForObject(url, null, CreatedTrelloCardDto.class);
//    }


//    public List<SpecialisationDto> getSpecialisations(int birthYear, Gender gender, int[] symptomsIds) throws Exception {
//        URI url = getSpecialisationsUri(birthYear, gender, symptomsIds);
//        System.out.println(url);
//
//        try {
//            Optional<SpecialisationDto[]> specialisationsResponse = Optional.ofNullable(restTemplate.getForObject(url, SpecialisationDto[].class));
//            System.out.println(url);
//            return Arrays.asList(specialisationsResponse.orElse(new SpecialisationDto[0]));
//        } catch (RestClientException e) {
//            LOGGER.error(e.getMessage(), e);
//            return new ArrayList<>();
//        }
//    }
//    private URI getSpecialisationsUri(int birthYear, Gender gender, int[] symptomsIds) throws Exception {
//        return UriComponentsBuilder.fromHttpUrl(medicConfig.getMedicApiEndpoint() + medicConfig.getSpecialisations())
//                .queryParam("token", medicConfig.getToken())
//                .queryParam("format", medicConfig.getFormat())
//                .queryParam("language", medicConfig.getLanguage())
//                .queryParam("year_of_birth", birthYear)
//                .queryParam("gender", gender)
//                .query("symptoms=" + Arrays.toString(symptomsIds))
//                .build().encode().toUri();
//    }
}
