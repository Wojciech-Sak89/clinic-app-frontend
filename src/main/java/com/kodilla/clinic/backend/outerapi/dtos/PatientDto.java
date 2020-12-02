package com.kodilla.clinic.backend.outerapi.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatientDto {
    private Integer patient_id = -1;
    private String name;
    private String surname;
    private String address;
    private LocalDate birthDate;
    private BigDecimal pesel;
    private BigDecimal telNum;
    private String email;
    private boolean inUrgency;
    private List<Integer> appointmentsIds = new ArrayList<>();
    private List<Integer> evaluationsIds = new ArrayList<>();

    public PatientDto(Integer patient_id,
                      String name,
                      String surname,
                      String address,
                      BigDecimal pesel,
                      BigDecimal telNum,
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

    @Override
    public String toString() {
        return "PatientDto{" +
                "patient_id=" + patient_id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", birthDate=" + birthDate +
                ", pesel=" + pesel +
                ", telNum=" + telNum +
                ", email='" + email + '\'' +
                ", inUrgency=" + inUrgency +
                ", appointmentsIds=" + appointmentsIds +
                ", evaluationsIds=" + evaluationsIds +
                '}';
    }
}
