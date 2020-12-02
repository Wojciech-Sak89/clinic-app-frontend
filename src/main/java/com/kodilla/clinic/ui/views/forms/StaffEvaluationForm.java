package com.kodilla.clinic.ui.views.forms;

import com.kodilla.clinic.backend.enums.Day;
import com.kodilla.clinic.backend.enums.Hour;
import com.kodilla.clinic.backend.enums.Stars;
import com.kodilla.clinic.backend.outerapi.dtos.DoctorDto;
import com.kodilla.clinic.backend.outerapi.dtos.PatientDto;
import com.kodilla.clinic.backend.outerapi.dtos.StaffEvaluationDto;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.EmergencyHourDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.views.StaffEvaluationsView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class StaffEvaluationForm extends FormLayout {

    private ComboBox<Integer> patient_Id = new ComboBox<>("Patient Id");
    private ComboBox<Integer> doctor_Id = new ComboBox<>("Doctor Id");
    private ComboBox<Stars> stars = new ComboBox<>("Rate");
    private DateTimePicker entryDate = new DateTimePicker("Creation date");
    private TextArea opinion = new TextArea("Opinion");

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");

    private Binder<StaffEvaluationDto> binder = new Binder<>(StaffEvaluationDto.class);

    private ClinicService clinicService;

    private StaffEvaluationsView staffEvaluationsView;

    public StaffEvaluationForm(StaffEvaluationsView staffEvaluationsView, ClinicService clinicService) {
        this.staffEvaluationsView = staffEvaluationsView;
        this.clinicService = clinicService;

        patient_Id.setItems(clinicService.getPatients().stream().map(PatientDto::getPatient_id));
        doctor_Id.setItems(clinicService.getDoctors().stream().map(DoctorDto::getDoctor_id));

        stars.setItems(Stars.values());

        binder.bindInstanceFields(this);

        clearForm();

        HorizontalLayout idsLayout = new HorizontalLayout(patient_Id, doctor_Id);
        HorizontalLayout starOpinionLayout = new HorizontalLayout(stars, opinion);
        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton);

        VerticalLayout evaluationLayout = new VerticalLayout(idsLayout, entryDate, starOpinionLayout, buttons);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> save());
        deleteButton.addClickListener(event -> delete());

        add(evaluationLayout);
    }

    private void save() {
        StaffEvaluationDto staffEvaluationDto = binder.getBean();

        if (patient_Id.isEmpty()) {
            staffEvaluationDto.setPatient_Id(-1);
        }
        if (doctor_Id.isEmpty()) {
            staffEvaluationDto.setDoctor_Id(-1);
        }

        if(clinicService.getDoctorById(staffEvaluationDto.getDoctor_Id()) != null
        && clinicService.getPatientById(staffEvaluationDto.getPatient_Id()) != null) {
            clinicService.saveStaffEvaluation(staffEvaluationDto);
        }

        staffEvaluationsView.refresh();
        setStaffEvaluationDto(null);
    }

    private void delete() {
        StaffEvaluationDto staffEvaluationDto = binder.getBean();
        clinicService.deleteStaffEvaluation(staffEvaluationDto.getEvaluation_id());
        staffEvaluationsView.refresh();
        setStaffEvaluationDto(null);
    }

    public void setStaffEvaluationDto(StaffEvaluationDto staffEvaluationDto) {
        binder.setBean(staffEvaluationDto);

        if (staffEvaluationDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
            stars.focus();
        }
    }

    private void clearForm() {
        this.binder.setBean(new StaffEvaluationDto());
    }
}