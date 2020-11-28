package com.kodilla.clinic.backend.outerapi.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatientDto {
    private Integer patient_id;
    private String name;
    private String surname;
    private String address;
    private LocalDate birthDate;
    private int pesel;
    private int telNum;
    private String email;
    private boolean inUrgency;
    private List<Integer> appointmentsIds;
    private List<Integer> evaluationsIds;

    public PatientDto(Integer patient_id,
                      String name,
                      String surname,
                      String address,
                      int pesel,
                      int telNum,
                      String email,
                      boolean inUrgency,
                      List<Integer> appointmentsIds,
                      List<Integer> evaluationsIds) {
        this.patient_id = patient_id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.pesel = pesel;
        this.telNum = telNum;
        this.email = email;
        this.inUrgency = inUrgency;
        this.appointmentsIds = appointmentsIds;
        this.evaluationsIds = evaluationsIds;
    }
}
