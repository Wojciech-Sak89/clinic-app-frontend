package com.kodilla.clinic.ui.views.forms;

import com.kodilla.clinic.backend.enums.Day;
import com.kodilla.clinic.backend.enums.Hour;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.ClinicDoctorScheduleDto;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.WorkingDayDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.views.WorkingDaysView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

import java.util.List;

public class WorkingDayForm extends FormLayout {

    private ComboBox<Day> day = new ComboBox<>("Weekday");
    private ComboBox<Hour> startHour = new ComboBox<>("Start of work");
    private ComboBox<Hour> endHour = new ComboBox<>("End of work");
    private ComboBox<ClinicDoctorScheduleDto> schedules = new ComboBox<>("Schedules Ids");

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");

    private Button addToScheduleButton = new Button("Add to schedule");
    private Button removeFromScheduleButton = new Button("Remove from schedule");

    private Binder<WorkingDayDto> binder = new BeanValidationBinder<>(WorkingDayDto.class);

    private ClinicService clinicService;

    private WorkingDaysView workingDaysView;

    public WorkingDayForm(WorkingDaysView workingDaysView, ClinicService clinicService) {
        this.workingDaysView = workingDaysView;
        this.clinicService = clinicService;

        binder.bindInstanceFields(this);

        day.setItems(Day.values());
        startHour.setItems(Hour.values());
        endHour.setItems(Hour.values());
        schedules.setItems(clinicService.getSchedules());
        schedules.setItemLabelGenerator(schedule -> String.valueOf(schedule.getSchedule_id()));

        clearForm();

        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton);
        HorizontalLayout scheduleButtons = new HorizontalLayout(addToScheduleButton, removeFromScheduleButton);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> save());

        deleteButton.addClickListener(event -> delete());

        addToScheduleButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        addToScheduleButton.addClickListener(event -> addScheduleToWorkingDay());

        removeFromScheduleButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        removeFromScheduleButton.addClickListener(event -> removeScheduleFromWorkingDay());

        add(day, startHour, endHour, buttons, schedules, scheduleButtons);
    }

    private void addScheduleToWorkingDay() {
        WorkingDayDto workingDayDto = binder.getBean();
        System.out.println("Selected Schedule_ID value: " + schedules.getValue().getSchedule_id());

        List<Integer> schedulesIds = workingDayDto.getSchedulesIds();
        schedulesIds.add(schedules.getValue().getSchedule_id());

        workingDayDto.setSchedulesIds(schedulesIds);

        clinicService.saveWorkingDay(workingDayDto);
        workingDaysView.refresh();
    }

    private void removeScheduleFromWorkingDay() {
        WorkingDayDto workingDayDto = binder.getBean();

        List<Integer> schedulesIds = workingDayDto.getSchedulesIds();
        schedulesIds.remove(schedules.getValue().getSchedule_id());

        workingDayDto.setSchedulesIds(schedulesIds);

        clinicService.saveWorkingDay(workingDayDto);
        workingDaysView.refresh();
    }

    private void save() {
        WorkingDayDto workingDayDto = binder.getBean();
        clinicService.saveWorkingDay(workingDayDto);
        workingDaysView.refresh();
        setWorkingDayDto(null);
    }

    private void delete() {
        WorkingDayDto workingDayDto = binder.getBean();
        clinicService.deleteWorkingDay(workingDayDto.getWorkingDay_id());
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
