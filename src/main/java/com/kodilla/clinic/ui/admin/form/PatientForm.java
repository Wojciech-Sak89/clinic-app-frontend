package com.kodilla.clinic.ui.admin.form;

import com.kodilla.clinic.backend.outerapi.dtos.PatientDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.admin.view.PatientsView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class PatientForm extends FormLayout {

    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Surname");
    private TextField address = new TextField("Address");
    private DatePicker birthDate = new DatePicker("Date of birth");
    private BigDecimalField pesel = new BigDecimalField("PESEL");
    private BigDecimalField telNum = new BigDecimalField("Tel. number");
    private EmailField email = new EmailField("Email");
    private ComboBox<Boolean> inUrgency = new ComboBox<>("Patient state");

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");
    private Button cancelButton = new Button("Cancel");

    private Binder<PatientDto> binder = new Binder<>(PatientDto.class);

    private ClinicService clinicService;

    private PatientsView patientsView;

    public PatientForm(PatientsView patientsView, ClinicService clinicService) {
        this.patientsView = patientsView;
        this.clinicService = clinicService;

        inUrgency.setItems(true, false);
        inUrgency.setItemLabelGenerator(bool -> bool ? "In urgency" : "Regular");

        binder.bindInstanceFields(this);

        clearForm();

        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> save());

        deleteButton.addClickListener(event -> delete());

        cancelButton.addClickListener(event -> this.setVisible(false));

        VerticalLayout formLayout = new VerticalLayout(
                name, surname, address, birthDate, pesel, telNum, email, inUrgency, buttons, cancelButton);
        add(formLayout);
    }

    private void save() {
        PatientDto patientDto = binder.getBean();

        clinicService.savePatient(patientDto);
        patientsView.refresh();
        setPatientDto(null);
    }

    private void delete() {
        PatientDto patientDto = binder.getBean();
        clinicService.deletePatient(patientDto.getPatient_id());
        patientsView.refresh();
        setPatientDto(null);
    }

    public void setPatientDto(PatientDto patientDto) {
        binder.setBean(patientDto);

        if (patientDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
            name.focus();
        }
    }

    private void clearForm() {
        this.binder.setBean(new PatientDto());
    }
}