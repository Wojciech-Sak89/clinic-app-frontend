package com.kodilla.clinic.backend.outerapi.dtos;

import com.kodilla.clinic.backend.enums.Stars;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StaffEvaluationDto {
    private Integer evaluation_id;
    private Stars stars;
    private String opinion;
    private LocalDateTime entryDate;
    private Integer patient_Id;
    private Integer doctor_Id;

    public StaffEvaluationDto(Integer evaluation_id, Stars stars, String opinion, Integer patient_Id, Integer doctor_Id) {
        this.evaluation_id = evaluation_id;
        this.stars = stars;
        this.opinion = opinion;
        this.patient_Id = patient_Id;
        this.doctor_Id = doctor_Id;
    }
}
