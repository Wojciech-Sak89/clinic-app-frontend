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
public class RecommendationDto {
    @JsonProperty("Name")
    private String specialisation;

    @JsonProperty("Accuracy")
    private String matchAccuracy;

    @Override
    public String toString() {
        return specialisation;
    }
}