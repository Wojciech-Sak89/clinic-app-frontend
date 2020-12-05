package com.kodilla.clinic.ui.views.forms;

import com.kodilla.clinic.backend.enums.Department;
import com.kodilla.clinic.backend.enums.Specialization;
import com.kodilla.clinic.backend.outerapi.dtos.DoctorDto;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.EmergencyHourDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.views.admin.DoctorsView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.List;

public class DoctorForm extends FormLayout {
    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Surname");
    private ComboBox<Specialization> specialization = new ComboBox<>("Specialization");
    private ComboBox<Department> department = new ComboBox<>("Department");
    private EmailField email = new EmailField("Email");
    private TextArea bio = new TextArea("Biographical note");

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");
    private Button cancelButton = new Button("Cancel");

    private Binder<DoctorDto> binder = new Binder<>(DoctorDto.class);

    private ClinicService clinicService;

    private DoctorsView doctorsView;

    public DoctorForm(DoctorsView doctorsView, ClinicService clinicService) {
        this.doctorsView = doctorsView;
        this.clinicService = clinicService;

        binder.bindInstanceFields(this);

        specialization.setItems(Specialization.values());
        department.setItems(Department.values());

        clearForm();

        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> save());

        deleteButton.addClickListener(event -> delete());

        cancelButton.addClickListener(event -> this.setVisible(false));

        VerticalLayout formLayout = new VerticalLayout(
                name, surname, specialization, department, email, bio, buttons, cancelButton);

        add(formLayout);
    }

    private void save() {
        DoctorDto doctorDto = binder.getBean();

        List<DoctorDto> allDoctors = clinicService.getDoctors();
        if (!allDoctors.contains(doctorDto)) {
            clinicService.saveDoctor(doctorDto);
        } else {
            Notification.show("This Doctor is already defined!");
        }

        doctorsView.refresh();
        setDoctorDto(null);
    }

    private void delete() {
        DoctorDto doctorDto = binder.getBean();
        clinicService.deleteDoctor(doctorDto.getDoctor_id());
        doctorsView.refresh();
        setDoctorDto(null);
    }

    public void setDoctorDto(DoctorDto doctorDto) {
        binder.setBean(doctorDto);

        if (doctorDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
            name.focus();
        }
    }

    private void clearForm() {
        this.binder.setBean(new DoctorDto());
    }
}
