package com.kodilla.clinic.ui.admin.form;

import com.kodilla.clinic.backend.enums.Day;
import com.kodilla.clinic.backend.enums.Hour;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.EmergencyHourDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.admin.view.EmergencyHoursView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;

import java.util.List;

public class EmergencyHourForm extends FormLayout {

    private ComboBox<Day> day = new ComboBox<>("Weekday");
    private ComboBox<Hour> hour = new ComboBox<>("Appointment time reserved for emergency only");

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");
    private Button cancelButton = new Button("Cancel");

    private Binder<EmergencyHourDto> binder = new Binder<>(EmergencyHourDto.class);

    private ClinicService clinicService;

    private EmergencyHoursView emergencyHoursView;

    public EmergencyHourForm(EmergencyHoursView emergencyHoursView, ClinicService clinicService) {
        this.emergencyHoursView = emergencyHoursView;
        this.clinicService = clinicService;

        binder.bindInstanceFields(this);

        day.setItems(Day.values());
        day.setClearButtonVisible(true);
        day.setRequired(true);
        hour.setItems(Hour.values());
        hour.setClearButtonVisible(true);
        hour.setRequired(true);
        hour.setMinWidth("20em");

        clearForm();

        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton);
        buttons.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> save());

        deleteButton.addClickListener(event -> delete());

        cancelButton.addClickListener(event -> this.setVisible(false));

        VerticalLayout emergencyLayout = new VerticalLayout(day, hour, buttons, cancelButton);
        add(emergencyLayout);
    }

    private void save() {
        if (!day.isEmpty() && !hour.isEmpty()) {
            EmergencyHourDto emergencyHourDto = binder.getBean();
            if (emergencyHourDto.getEmergencyHour_id() != null) {
                update(emergencyHourDto);
            } else {
                List<EmergencyHourDto> allEmergencyHours = clinicService.getEmergencyHours();
                if (!allEmergencyHours.contains(emergencyHourDto)) {
                    clinicService.saveEmergencyHour(emergencyHourDto);
                } else {
                    Notification.show("This Emergency Hour is already defined!");
                }
            }
        }

        emergencyHoursView.refresh();
        setEmergencyHourDto(null);
    }

    private void update(EmergencyHourDto emergencyHourDto) {
        System.out.println(
                "EmergencyHourForm.update(): " + emergencyHourDto + " ID: "  + emergencyHourDto.getEmergencyHour_id());

        List<EmergencyHourDto> allEmergencyHours = clinicService.getEmergencyHours();
        if (!allEmergencyHours.contains(emergencyHourDto)) {
            clinicService.updateEmergencyHour(emergencyHourDto);
        } else {
            Notification.show("This Emergency Hour is already defined!");
        }
    }

    private void delete() {
        EmergencyHourDto emergencyHourDto = binder.getBean();

        List<EmergencyHourDto> allEmergencyHours = clinicService.getEmergencyHours();
        EmergencyHourDto hourToDelete = allEmergencyHours.stream()
                .filter(emergencyHourDto_oneOfAll -> emergencyHourDto_oneOfAll.equals(emergencyHourDto))
                .findAny()
                .get();

        int hourToDelete_DependentSchedules = hourToDelete.getSchedulesIds().size();

        if (hourToDelete_DependentSchedules == 0) {
            clinicService.deleteEmergencyHour(emergencyHourDto.getEmergencyHour_id());
        } else {
            Notification.show("Cannot be deleted, used in schedule(s). Action: delete from schedule(s) using it.");
        }

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
