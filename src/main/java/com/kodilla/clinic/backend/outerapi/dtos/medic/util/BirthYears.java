package com.kodilla.clinic.backend.outerapi.dtos.medic.util;

import java.util.ArrayList;
import java.util.List;

public class BirthYears {
    public static List<Integer> getYears() {
        List<Integer> years = new ArrayList<>();
        for (int i = 2020; i >= 1880; i--) {
            years.add(i);
        }
        return years;
    }
}
