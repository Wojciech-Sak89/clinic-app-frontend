package com.kodilla.clinic.ui.admin.form;

import com.kodilla.clinic.backend.enums.Status;
import com.kodilla.clinic.backend.outerapi.dtos.AppointmentDto;
import com.kodilla.clinic.backend.outerapi.dtos.DoctorDto;
import com.kodilla.clinic.backend.outerapi.dtos.PatientDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.admin.view.AppointmentsView;
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

public class AppointmentsForm extends FormLayout {
    private TextArea patientDataTextArea = new TextArea("Patient");
    private TextArea doctorDataTextArea = new TextArea("Doctor");
    private ComboBox<Integer> patient_Id = new ComboBox<>("Patient Id");
    private ComboBox<Integer> doctor_Id = new ComboBox<>("Doctor Id");
    private DateTimePicker dateTime = new DateTimePicker("Appointment date");
    private ComboBox<Boolean> forEmergency = new ComboBox<>("Set emergency restrictions");
    private ComboBox<Status> status = new ComboBox<>("Status");

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");
    private Button cancelButton = new Button("Cancel");

    private Binder<AppointmentDto> binder = new Binder<>(AppointmentDto.class);

    private ClinicService clinicService;

    private AppointmentsView appointmentsView;

    public AppointmentsForm(AppointmentsView appointmentsView, ClinicService clinicService) {
        this.appointmentsView = appointmentsView;
        this.clinicService = clinicService;

        patient_Id.setItems(clinicService.getPatients().stream().map(PatientDto::getPatient_id));
        patient_Id.addValueChangeListener(event ->  setPatientDataTextField());
        patient_Id.setClearButtonVisible(true);

        doctor_Id.setItems(clinicService.getDoctors().stream().map(DoctorDto::getDoctor_id));
        doctor_Id.addValueChangeListener(event ->  setDoctorDataTextField());
        doctor_Id.setClearButtonVisible(true);
        doctor_Id.setRequired(true);

        status.setItems(Status.values());
        status.setRequired(true);

        forEmergency.setItems(true, false);
        forEmergency.setItemLabelGenerator(bool -> bool ? "For emergency only" : "For all patients");
        forEmergency.addValueChangeListener(event -> {
            if (!forEmergency.isEmpty()) {
                if (forEmergency.getValue().equals(true)) {
                    status.setValue(Status.FOR_EMERGENCY_ONLY);
                }
            }
        });

        binder.bindInstanceFields(this);

        clearForm();

        doctorDataTextArea.setReadOnly(true);
        patientDataTextArea.setReadOnly(true);

        setPatientDataTextField();
        setDoctorDataTextField();

        HorizontalLayout idsLayout = new HorizontalLayout(patient_Id, doctor_Id);
        HorizontalLayout emergencyStatusLayout = new HorizontalLayout(forEmergency, status);
        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton);

        VerticalLayout evaluationLayout = new VerticalLayout(
                patientDataTextArea, doctorDataTextArea,
                idsLayout, dateTime, emergencyStatusLayout, buttons, cancelButton);
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
        if (!doctor_Id.isEmpty() && !status.isEmpty()) {
            AppointmentDto appointmentDto = binder.getBean();

            if (patient_Id.isEmpty()) {
                appointmentDto.setPatientId(-1);
            }
            if (doctor_Id.isEmpty()) {
                appointmentDto.setDoctorId(-1);
            }

            if(clinicService.getDoctorById(appointmentDto.getDoctorId()) != null) {
                clinicService.saveAppointment(appointmentDto);
            } else {
                Notification.show("Existing doctor is needed to set an appointment!");
            }
        } else {
            Notification.show("Doctor ID and appointment Status is required");
        }

        appointmentsView.refresh();
        setAppointmentDto(null);
    }

    private void delete() {
        AppointmentDto appointmentDto = binder.getBean();
        clinicService.deleteAppointment(appointmentDto.getAppointment_id());
        appointmentsView.refresh();
        setAppointmentDto(null);
    }

    public void setAppointmentDto(AppointmentDto appointmentDto) {
        binder.setBean(appointmentDto);

        setPatientDataTextField();
        setDoctorDataTextField();

        if (appointmentDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
            status.focus();
        }
    }

    private void clearForm() {
        this.binder.setBean(new AppointmentDto());
    }
}
