package com.kodilla.clinic.backend.outerapi.dtos.schedule;

import com.kodilla.clinic.backend.enums.Day;
import com.kodilla.clinic.backend.enums.Hour;
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
public class EmergencyHourDto {
    private Integer emergencyHour_id;
    private Day day;
    private Hour hour;
    private List<Integer> schedulesIds = new ArrayList<>();

    @Override
    public String toString() {
        return  "       " + day + ": " + hour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmergencyHourDto that = (EmergencyHourDto) o;

        if (day != that.day) return false;
        return hour == that.hour;
    }
}
