package com.kodilla.clinic.ui.patient;

import com.kodilla.clinic.backend.enums.Gender;
import com.kodilla.clinic.backend.outerapi.dtos.medic.SymptomDto;
import com.kodilla.clinic.backend.outerapi.dtos.medic.util.BirthYears;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route(value = "specialistRecommendation", layout = MainLayout.class)
@PageTitle("Specialist recommendation | Clinic App")
public class RecommendationFormView extends FormLayout {

    private ComboBox<Integer> birthYearComboBox = new ComboBox<>("Year of birth");
    private ComboBox<Gender> genderComboBox = new ComboBox<>("Gender");

    private ComboBox<SymptomDto> symptom1ComboBox = new ComboBox<>("Symptom 1");
    private ComboBox<SymptomDto> symptom2ComboBox = new ComboBox<>("Symptom 2");

    private TextArea recommendationText = new TextArea("Recommended specialists");

    private Button generateRecommendationButton = new Button("Get recommendation");

    private ClinicService clinicService;

    public RecommendationFormView(ClinicService clinicService) {
        this.clinicService = clinicService;

        birthYearComboBox.setItems(BirthYears.getYears());
        birthYearComboBox.setClearButtonVisible(true);
        birthYearComboBox.setRequired(true);

        genderComboBox.setItems(Gender.values());
        genderComboBox.setClearButtonVisible(true);
        genderComboBox.setRequired(true);

        symptom1ComboBox.setItems(clinicService.getSymptoms());
        symptom1ComboBox.setItemLabelGenerator(SymptomDto::getName);
        symptom1ComboBox.setClearButtonVisible(true);
        symptom1ComboBox.setRequired(true);

        symptom2ComboBox.setItems(clinicService.getSymptoms());
        symptom2ComboBox.setItemLabelGenerator(SymptomDto::getName);
        symptom2ComboBox.setClearButtonVisible(true);

        recommendationText.setReadOnly(true);

        generateRecommendationButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        generateRecommendationButton.addClickListener(event -> generateRecommendation());


        HorizontalLayout patientDataLayout = new HorizontalLayout(birthYearComboBox, genderComboBox);
        HorizontalLayout symptomsLayout = new HorizontalLayout(symptom1ComboBox, symptom2ComboBox);
        HorizontalLayout recommendationsLayout = new HorizontalLayout(recommendationText, generateRecommendationButton);
        recommendationsLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        VerticalLayout verticalLayout = new VerticalLayout(patientDataLayout, symptomsLayout, recommendationsLayout);
        verticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.START);

        add(verticalLayout);

    }

    private void generateRecommendation() {
        List<Integer> symptomsIds = new ArrayList<>();

        if(!symptom1ComboBox.isEmpty())
            symptomsIds.add(symptom1ComboBox.getValue().getId());
        if(!symptom2ComboBox.isEmpty())
            symptomsIds.add(symptom2ComboBox.getValue().getId());

        if (symptom1ComboBox.isEmpty() && symptom2ComboBox.isEmpty())
            Notification.show("At least one symptom is required to get recommendation");

        int[] symptomsIdsInts = new int[symptomsIds.size()];
        int i = 0;
        for (Integer n : symptomsIds) {
            symptomsIdsInts[i++] = n;
        }

        recommendationText.setValue(clinicService.getRecommendations(
                birthYearComboBox.getValue(), genderComboBox.getValue(), symptomsIdsInts));
    }
}