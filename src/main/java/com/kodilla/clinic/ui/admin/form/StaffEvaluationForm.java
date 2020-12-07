package com.kodilla.clinic.ui.admin.form;

import com.kodilla.clinic.backend.enums.Stars;
import com.kodilla.clinic.backend.outerapi.dtos.DoctorDto;
import com.kodilla.clinic.backend.outerapi.dtos.PatientDto;
import com.kodilla.clinic.backend.outerapi.dtos.StaffEvaluationDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.admin.view.StaffEvaluationsView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;

public class StaffEvaluationForm extends FormLayout {

    private TextArea patientDataTextArea = new TextArea("Patient");
    private TextArea doctorDataTextArea = new TextArea("Doctor");
    private ComboBox<Integer> patient_Id = new ComboBox<>("Patient Id");
    private ComboBox<Integer> doctor_Id = new ComboBox<>("Doctor Id");
    private ComboBox<Stars> stars = new ComboBox<>("Rate");
    private DateTimePicker entryDate = new DateTimePicker("Creation date");
    private TextArea opinion = new TextArea("Opinion");

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");
    private Button cancelButton = new Button("Cancel");

    private Binder<StaffEvaluationDto> binder = new Binder<>(StaffEvaluationDto.class);

    private ClinicService clinicService;

    private StaffEvaluationsView staffEvaluationsView;

    public StaffEvaluationForm(StaffEvaluationsView staffEvaluationsView, ClinicService clinicService) {
        this.staffEvaluationsView = staffEvaluationsView;
        this.clinicService = clinicService;

        patient_Id.setItems(clinicService.getPatients().stream().map(PatientDto::getPatient_id));
        patient_Id.addValueChangeListener(event ->  setPatientDataTextField());
        patient_Id.setClearButtonVisible(true);
        patient_Id.setRequired(true);

        doctor_Id.setItems(clinicService.getDoctors().stream().map(DoctorDto::getDoctor_id));
        doctor_Id.addValueChangeListener(event ->  setDoctorDataTextField());
        doctor_Id.setClearButtonVisible(true);
        doctor_Id.setRequired(true);

        stars.setItems(Stars.values());
        stars.setClearButtonVisible(true);

        binder.bindInstanceFields(this);

        clearForm();

        doctorDataTextArea.setReadOnly(true);
        patientDataTextArea.setReadOnly(true);

        setPatientDataTextField();
        setDoctorDataTextField();

        HorizontalLayout idsLayout = new HorizontalLayout(patient_Id, doctor_Id);
        HorizontalLayout starOpinionLayout = new HorizontalLayout(stars, opinion);
        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton);

        VerticalLayout evaluationLayout = new VerticalLayout(
                patientDataTextArea, doctorDataTextArea,
                idsLayout, entryDate, starOpinionLayout, buttons, cancelButton);
        patientDataTextArea.setMinWidth("25em");
        doctorDataTextArea.setMinWidth("25em");

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> save());
        deleteButton.addClickListener(event -> delete());
        cancelButton.addClickListener(event -> this.setVisible(false));

        add(evaluationLayout);
    }

    private void setDoctorDataTextField() {
        if (!doctor_Id.isEmpty()) {
            if(doctor_Id.getValue() > 0) {
                DoctorDto currDoctor = clinicService.getDoctorById(doctor_Id.getValue());
                doctorDataTextArea.setValue(
                        currDoctor.getName() + " " + currDoctor.getSurname() +
                                "\n Depratment: " + currDoctor.getDepartment() +
                                "\n Email: " + currDoctor.getEmail());
            }
        } else {
            doctorDataTextArea.setValue("");
        }
    }

    private void setPatientDataTextField() {
        if (!patient_Id.isEmpty()) {
            if(patient_Id.getValue() > 0) {
                PatientDto currPatient = clinicService.getPatientById(patient_Id.getValue());
                patientDataTextArea.setValue(
                        currPatient.getName() + " " + currPatient.getSurname() + "\n PESEL: " + currPatient.getPesel());
            }
        } else {
            patientDataTextArea.setValue("");
        }
    }

    private void save() {
        if (!patient_Id.isEmpty() && doctor_Id.isEmpty()) {
            StaffEvaluationDto staffEvaluationDto = binder.getBean();

            if(clinicService.getDoctorById(staffEvaluationDto.getDoctor_Id()) != null
                    && clinicService.getPatientById(staffEvaluationDto.getPatient_Id()) != null) {
                clinicService.saveStaffEvaluation(staffEvaluationDto);
            }
        } else {
            Notification.show("Patient ID and Doctor Id required");
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
        setPatientDataTextField();
        setDoctorDataTextField();

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