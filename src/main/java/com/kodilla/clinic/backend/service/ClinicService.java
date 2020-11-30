package com.kodilla.clinic.backend.service;

import com.kodilla.clinic.backend.enums.Day;
import com.kodilla.clinic.backend.enums.Department;
import com.kodilla.clinic.backend.enums.Specialization;
import com.kodilla.clinic.backend.outerapi.client.ClinicClient;
import com.kodilla.clinic.backend.outerapi.dtos.DoctorDto;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.ClinicDoctorScheduleDto;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.EmergencyHourDto;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.WorkingDayDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClinicService {
    @Autowired
    private ClinicClient clinicClient;

    //WORKING DAYS
    public List<WorkingDayDto> getWorkingDays() {
        if (clinicClient.getWorkingDays() != null) {
            return clinicClient.getWorkingDays();
        } else
        return new ArrayList<>();
    }

    public List<WorkingDayDto> getWorkingDays_ByWeekday(Day day) {
        if (clinicClient.getWorkingDays() != null) {
            return clinicClient.getWorkingDays().stream()
                    .filter(workingDayDto -> workingDayDto.getDay().equals(day))
                    .collect(Collectors.toList());
        } else
            return new ArrayList<>();
    }

    public void deleteWorkingDay(Integer id) {
        clinicClient.deleteWorkingDayById(id);
    }

    public void saveWorkingDay(WorkingDayDto workingDayDto) {
        clinicClient.saveWorkingDay(workingDayDto);
    }

    //SCHEDULES
    public List<ClinicDoctorScheduleDto> getSchedules() {
        if (clinicClient.getSchedules() != null) {
            return clinicClient.getSchedules();
        } else
            return new ArrayList<>();
    }

    //EMERGENCY HOURS
    public void saveEmergencyHour(EmergencyHourDto emergencyHourDto) {
        clinicClient.saveEmergencyHour(emergencyHourDto);
    }

    public void deleteEmergencyHour(Integer emergencyHour_id) {
        clinicClient.deleteEmergencyHourById(emergencyHour_id);
    }

    public List<EmergencyHourDto> getEmergencyHours() {
        if (clinicClient.getEmergencyHours() != null) {
            return clinicClient.getEmergencyHours();
        } else
            return new ArrayList<>();
    }

    public List<EmergencyHourDto> getEmergencyHours_ByWeekday(Day day) {
        if (clinicClient.getEmergencyHours() != null) {
            return clinicClient.getEmergencyHours().stream()
                    .filter(emergencyHourDto -> emergencyHourDto.getDay().equals(day))
                    .collect(Collectors.toList());
        } else
            return new ArrayList<>();
    }

    //DOCTORS
    public void saveDoctor(DoctorDto doctorDto) {
        clinicClient.saveDoctor(doctorDto);
    }

    public void deleteDoctor(Integer doctor_Id) {
        clinicClient.deleteDoctorById(doctor_Id);
    }

    public List<DoctorDto> getDoctors() {
        if (clinicClient.getDoctors() != null) {
            return clinicClient.getDoctors();
        } else
            return new ArrayList<>();
    }

    public List<DoctorDto> getDoctors_ByDepartment(Department department) {
        if (clinicClient.getDoctors() != null) {
            return clinicClient.getDoctors().stream()
                    .filter(doctorDto -> doctorDto.getDepartment().equals(department))
                    .collect(Collectors.toList());
        } else
            return new ArrayList<>();
    }

    public List<DoctorDto> getDoctors_BySpecialization(Specialization specialization) {
        if (clinicClient.getDoctors() != null) {
            return clinicClient.getDoctors().stream()
                    .filter(doctorDto -> doctorDto.getSpecialization().equals(specialization))
                    .collect(Collectors.toList());
        } else
            return new ArrayList<>();
    }
}
