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
    private Integer evaluation_id = -1;
    private Stars stars;
    private String opinion;
    private LocalDateTime entryDate = LocalDateTime.now();
    private Integer patient_Id = 0;
    private Integer doctor_Id = 0;

    public StaffEvaluationDto(Integer evaluation_id, Stars stars, String opinion, Integer patient_Id, Integer doctor_Id) {
        this.evaluation_id = evaluation_id;
        this.stars = stars;
        this.opinion = opinion;
        this.patient_Id = patient_Id;
        this.doctor_Id = doctor_Id;
    }

    @Override
    public String toString() {
        return "StaffEvaluationDto{" +
                "evaluation_id=" + evaluation_id +
                ", stars=" + stars +
                ", opinion='" + opinion + '\'' +
                ", entryDate=" + entryDate +
                ", patient_Id=" + patient_Id +
                ", doctor_Id=" + doctor_Id +
                '}';
    }
}
