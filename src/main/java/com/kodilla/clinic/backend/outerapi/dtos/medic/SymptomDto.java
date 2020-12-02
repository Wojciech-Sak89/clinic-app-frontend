package com.kodilla.clinic.backend.outerapi.dtos.medic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SymptomDto {
    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("Name")
    private String name;

    @Override
    public String toString() {
        return "SymptomDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
