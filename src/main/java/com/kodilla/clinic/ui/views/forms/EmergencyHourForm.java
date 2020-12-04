package com.kodilla.clinic.ui.views.forms;

import com.kodilla.clinic.backend.enums.Day;
import com.kodilla.clinic.backend.enums.Hour;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.EmergencyHourDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.views.admin.EmergencyHoursView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;

public class EmergencyHourForm extends FormLayout {

    private ComboBox<Day> day = new ComboBox<>("Weekday");
    private ComboBox<Hour> hour = new ComboBox<>("Appointment time reserved for emergency only");

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");

    private Binder<EmergencyHourDto> binder = new Binder<>(EmergencyHourDto.class);

    private ClinicService clinicService;

    private EmergencyHoursView emergencyHoursView;

    public EmergencyHourForm(EmergencyHoursView emergencyHoursView, ClinicService clinicService) {
        this.emergencyHoursView = emergencyHoursView;
        this.clinicService = clinicService;

        binder.bindInstanceFields(this);

        day.setItems(Day.values());
        hour.setItems(Hour.values());

        clearForm();

        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> save());

        deleteButton.addClickListener(event -> delete());

        add(day, hour, buttons);
    }

    private void save() {
        EmergencyHourDto emergencyHourDto = binder.getBean();

        System.out.println("EmergencyHourForm.save() DAY : " + emergencyHourDto.getDay());
        System.out.println("EmergencyHourForm.save() HOUR : " + emergencyHourDto.getHour());

        clinicService.saveEmergencyHour(emergencyHourDto);
        emergencyHoursView.refresh();
        setEmergencyHourDto(null);
    }

    private void delete() {
        EmergencyHourDto emergencyHourDto = binder.getBean();
        clinicService.deleteEmergencyHour(emergencyHourDto.getEmergencyHour_id());
        emergencyHoursView.refresh();
        setEmergencyHourDto(null);
    }

    public void setEmergencyHourDto(EmergencyHourDto emergencyHourDto) {
        binder.setBean(emergencyHourDto);

        if (emergencyHourDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
            day.focus();
        }
    }

    private void clearForm() {
        this.binder.setBean(new EmergencyHourDto());
    }
}
