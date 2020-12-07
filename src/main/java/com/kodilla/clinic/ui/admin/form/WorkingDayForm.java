package com.kodilla.clinic.ui.admin.form;

import com.kodilla.clinic.backend.enums.Day;
import com.kodilla.clinic.backend.enums.Hour;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.WorkingDayDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.admin.view.WorkingDaysView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

import java.util.List;

public class WorkingDayForm extends FormLayout {

    private ComboBox<Day> day = new ComboBox<>("Weekday");
    private ComboBox<Hour> startHour = new ComboBox<>("Start of work");
    private ComboBox<Hour> endHour = new ComboBox<>("End of work");

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");
    private Button cancelButton = new Button("Cancel");

    private Binder<WorkingDayDto> binder = new BeanValidationBinder<>(WorkingDayDto.class);

    private ClinicService clinicService;

    private WorkingDaysView workingDaysView;

    public WorkingDayForm(WorkingDaysView workingDaysView, ClinicService clinicService) {
        this.workingDaysView = workingDaysView;
        this.clinicService = clinicService;

        binder.bindInstanceFields(this);

        day.setItems(Day.values());
        day.setClearButtonVisible(true);
        day.setRequired(true);
        startHour.setItems(Hour.values());
        startHour.setClearButtonVisible(true);
        startHour.setRequired(true);
        endHour.setItems(Hour.values());
        endHour.setClearButtonVisible(true);
        endHour.setRequired(true);

        clearForm();

        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton);
        buttons.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> save());

        deleteButton.addClickListener(event -> delete());

        cancelButton.addClickListener(event -> this.setVisible(false));

        VerticalLayout formMainLayout = new VerticalLayout(day, startHour, endHour, buttons, cancelButton);

        add(formMainLayout);
    }

    private void save() {
        if(!day.isEmpty() && !startHour.isEmpty() && !endHour.isEmpty()) {
            WorkingDayDto workingDayDto = binder.getBean();

            List<WorkingDayDto> allWorkingDays = clinicService.getWorkingDays();
            if (!allWorkingDays.contains(workingDayDto)) {
                clinicService.saveWorkingDay(workingDayDto);
            } else {
                Notification.show("This working day is already defined!");
            }
        }

        workingDaysView.refresh();
        setWorkingDayDto(null);
    }

    private void delete() {
        WorkingDayDto workingDayDto = binder.getBean();

        List<WorkingDayDto> allWorkingDays = clinicService.getWorkingDays();
        WorkingDayDto dayToDelete = allWorkingDays.stream()
                .filter(workingDayDto_oneOfAll -> workingDayDto_oneOfAll.equals(workingDayDto))
                .findAny()
                .get();

        int dayToDelete_DependetnSchedules = dayToDelete.getSchedulesIds().size();

        if (dayToDelete_DependetnSchedules == 0) {
            clinicService.deleteWorkingDay(workingDayDto.getWorkingDay_id());
        } else {
            Notification.show("Cannot be deleted, used in schedule(s). Action: delete from schedule(s) using it.");
        }

        workingDaysView.refresh();
        setWorkingDayDto(null);
    }

    public void setWorkingDayDto(WorkingDayDto workingDayDto) {
        binder.setBean(workingDayDto);

        if (workingDayDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
            day.focus();
        }
    }

    private void clearForm() {
        this.binder.setBean(new WorkingDayDto());
    }
}
