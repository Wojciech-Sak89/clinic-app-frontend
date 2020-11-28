package com.kodilla.clinic.backend.service;

import com.kodilla.clinic.backend.enums.Day;
import com.kodilla.clinic.backend.outerapi.client.ClinicClient;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.ClinicDoctorScheduleDto;
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

    public WorkingDayDto saveWorkingDay(WorkingDayDto workingDayDto) {
        return clinicClient.saveWorkingDay(workingDayDto);
    }

    public List<ClinicDoctorScheduleDto> getSchedules() {
        if (clinicClient.getSchedules() != null) {
            return clinicClient.getSchedules();
        } else
            return new ArrayList<>();
    }
}
