package com.kodilla.clinic.backend.outerapi.dtos.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClinicDoctorScheduleDto that = (ClinicDoctorScheduleDto) o;

        if (!Objects.equals(workingDaysDtos, that.workingDaysDtos))
            return false;
        return Objects.equals(emergencyHoursDtos, that.emergencyHoursDtos);
    }

    @Override
    public int hashCode() {
        int result = workingDaysDtos != null ? workingDaysDtos.hashCode() : 0;
        result = 31 * result + (emergencyHoursDtos != null ? emergencyHoursDtos.hashCode() : 0);
        return result;
    }
}
