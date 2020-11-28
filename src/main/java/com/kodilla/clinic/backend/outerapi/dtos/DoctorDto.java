package com.kodilla.clinic.backend.outerapi.dtos;

import com.kodilla.clinic.backend.enums.Department;
import com.kodilla.clinic.backend.enums.Specialization;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.ClinicDoctorScheduleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoctorDto {
    private Integer doctor_id;
    private String name;
    private String surname;
    private Specialization specialization;
    private Department department;
    private String email;
    private ClinicDoctorScheduleDto clinicDoctorScheduleDto;
    private String bio;
    private List<Integer> appointmentsIds;
    private List<Integer> evaluationsIds;
}
