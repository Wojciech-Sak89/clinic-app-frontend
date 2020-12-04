package com.kodilla.clinic.ui.views.forms;

import com.kodilla.clinic.backend.enums.Status;
import com.kodilla.clinic.backend.outerapi.dtos.AppointmentDto;
import com.kodilla.clinic.backend.outerapi.dtos.DoctorDto;
import com.kodilla.clinic.backend.outerapi.dtos.PatientDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.views.admin.AppointmentsView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;

public class AppointmentsForm extends FormLayout {
    private ComboBox<Integer> patient_Id = new ComboBox<>("Patient Id");
    private ComboBox<Integer> doctor_Id = new ComboBox<>("Doctor Id");
    private DateTimePicker dateTime = new DateTimePicker("Appointment date");
    private ComboBox<Boolean> forEmergency = new ComboBox<>("Set emergency restrictions");
    private ComboBox<Status> status = new ComboBox<>("Status");

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");

    private Binder<AppointmentDto> binder = new Binder<>(AppointmentDto.class);

    private ClinicService clinicService;

    private AppointmentsView appointmentsView;

    public AppointmentsForm(AppointmentsView appointmentsView, ClinicService clinicService) {
        this.appointmentsView = appointmentsView;
        this.clinicService = clinicService;

        patient_Id.setItems(clinicService.getPatients().stream().map(PatientDto::getPatient_id));
        doctor_Id.setItems(clinicService.getDoctors().stream().map(DoctorDto::getDoctor_id));

        status.setItems(Status.values());

        forEmergency.setItems(true, false);
        forEmergency.setItemLabelGenerator(bool -> bool ? "For emergency only" : "For all patients");

        binder.bindInstanceFields(this);

        clearForm();

        HorizontalLayout idsLayout = new HorizontalLayout(patient_Id, doctor_Id);
        HorizontalLayout emergencyStatusLayout = new HorizontalLayout(forEmergency, status);
        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton);

        VerticalLayout evaluationLayout = new VerticalLayout(idsLayout, dateTime, emergencyStatusLayout, buttons);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> save());
        deleteButton.addClickListener(event -> delete());

        add(evaluationLayout);
    }

    private void save() {
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
            Notification.show("Doctor is needed to set an appointment!");
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
