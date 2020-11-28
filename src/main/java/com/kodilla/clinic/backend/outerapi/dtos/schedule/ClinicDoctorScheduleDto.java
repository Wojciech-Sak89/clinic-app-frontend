package com.kodilla.clinic.backend.outerapi.dtos.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClinicDoctorScheduleDto {
    private Integer schedule_id;
    private List<WorkingDayDto> workingDaysDtos;
    private List<EmergencyHourDto> emergencyHoursDtos;
}
