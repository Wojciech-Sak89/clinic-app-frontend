package com.kodilla.clinic.backend.outerapi.dtos.schedule;

import com.kodilla.clinic.backend.enums.Day;
import com.kodilla.clinic.backend.enums.Hour;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmergencyHourDto {
    private Integer emergencyHour_id;
    private Day day;
    private Hour hour;
    private List<Integer> schedulesIds;
}
