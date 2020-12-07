package com.kodilla.clinic.backend.outerapi.dtos;

import com.kodilla.clinic.backend.enums.Department;
import com.kodilla.clinic.backend.enums.Specialization;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.ClinicDoctorScheduleDto;
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
public class DoctorDto {
    private Integer doctor_id = -1;
    private String name;
    private String surname;
    private Specialization specialization;
    private Department department;
    private String email;
    private ClinicDoctorScheduleDto clinicDoctorScheduleDto = new ClinicDoctorScheduleDto();
    private String bio;
    private List<Integer> appointmentsIds = new ArrayList<>();
    private List<Integer> evaluationsIds = new ArrayList<>();

    @Override
    public String toString() {
        return "DoctorDto{" +
                "doctor_id=" + doctor_id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", specialization=" + specialization +
                ", department=" + department +
                ", email='" + email + '\'' +
                ", clinicDoctorScheduleDto=" + clinicDoctorScheduleDto +
                ", bio='" + bio + '\'' +
                ", appointmentsIds=" + appointmentsIds +
                ", evaluationsIds=" + evaluationsIds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoctorDto doctorDto = (DoctorDto) o;

        if (!name.equals(doctorDto.name)) return false;
        if (!surname.equals(doctorDto.surname)) return false;
        return email.equals(doctorDto.email);
    }
}
