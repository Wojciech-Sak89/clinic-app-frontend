package com.kodilla.clinic.ui.views.forms;

import com.kodilla.clinic.backend.enums.Department;
import com.kodilla.clinic.backend.enums.Specialization;
import com.kodilla.clinic.backend.outerapi.dtos.DoctorDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.views.DoctorsView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class DoctorForm extends FormLayout {
    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Surname");
    private ComboBox<Specialization> specialization = new ComboBox<>("Specialization");
    private ComboBox<Department> department = new ComboBox<>("Department");
    private EmailField email = new EmailField("Email");
    private TextField bio = new TextField("Biographical note");

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");

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

        add(name, surname, specialization, department, email, bio, buttons);
    }

    private void save() {
        DoctorDto doctorDto = binder.getBean();

        clinicService.saveDoctor(doctorDto);
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
