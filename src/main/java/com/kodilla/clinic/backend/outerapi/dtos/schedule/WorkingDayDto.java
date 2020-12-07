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
public class WorkingDayDto {
    private Integer workingDay_id;
    private Day day;
    private Hour startHour;
    private Hour endHour;
    private List<Integer> schedulesIds = new ArrayList<>();

    @Override
    public String toString() {
        return  "       " + day +
                "\n             " + startHour + " - " + endHour + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkingDayDto that = (WorkingDayDto) o;

        if (day != that.day) return false;
        if (startHour != that.startHour) return false;
        return endHour == that.endHour;
    }
}
