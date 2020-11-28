package com.kodilla.clinic.backend.outerapi.dtos;

import com.kodilla.clinic.backend.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentDto {
    private Integer appointment_id;
    private boolean forEmergency;
    private LocalDateTime dateTime;
    private Status status;
    private Integer doctorId;
    private Integer patientId;

    public AppointmentDto(Integer appointment_id, boolean forEmergency, Status status, Integer doctorId, Integer patientId) {
        this.appointment_id = appointment_id;
        this.forEmergency = forEmergency;
        this.status = status;
        this.doctorId = doctorId;
        this.patientId = patientId;
    }
}
