package com.kodilla.clinic.backend.outerapi.dtos.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClinicDoctorScheduleDto {
    private Integer schedule_id = -1;
    private List<WorkingDayDto> workingDaysDtos = new ArrayList<>();
    private List<EmergencyHourDto> emergencyHoursDtos = new ArrayList<>();

    @Override
    public String toString() {
            return "WORKING DAYS: \n" + workingDaysDtos +
                "\nEMERGENCY HOURS: \n" + emergencyHoursDtos;
    }
}
