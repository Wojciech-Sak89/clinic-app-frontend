package com.kodilla.clinic.ui.admin.form;

import com.kodilla.clinic.backend.enums.Specialization;
import com.kodilla.clinic.backend.outerapi.dtos.DoctorDto;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.EmergencyHourDto;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.WorkingDayDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.admin.view.DoctorScheduleView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.List;

public class DoctorScheduleForm extends FormLayout {
    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Surname");
    private ComboBox<Specialization> specialization = new ComboBox<>("Specialization");
    private EmailField email = new EmailField("Email");
    private TextArea schedule;

    private ComboBox<WorkingDayDto> workingDayComboBox = new ComboBox<>("Working days");
    private ComboBox<EmergencyHourDto> emergencyHourComboBox = new ComboBox<>("Emergency hours");

    private Binder<DoctorDto> binder = new Binder<>(DoctorDto.class);

    private ClinicService clinicService;

    private DoctorScheduleView doctorScheduleView;

    private Button addDayButton = new Button("Add to schedule");
    private Button removeDayButton = new Button("Remove from schedule");

    private Button addEmHourButton = new Button("Add to schedule");
    private Button removeEmHourButton = new Button("Remove from schedule");

    private Button cancelButton = new Button("Cancel");


    public DoctorScheduleForm(DoctorScheduleView doctorScheduleView, ClinicService clinicService) {
        this.doctorScheduleView = doctorScheduleView;
        this.clinicService = clinicService;

        name.setReadOnly(true);
        surname.setReadOnly(true);
        specialization.setReadOnly(true);
        email.setReadOnly(true);

        schedule = new TextArea("Schedule");
        schedule.setReadOnly(true);

        workingDayComboBox.setItems(clinicService.getWorkingDays());
        workingDayComboBox.setItemLabelGenerator(workingDayDto ->
                workingDayDto.getDay() + ": " + workingDayDto.getStartHour() + " - " + workingDayDto.getEndHour());
        workingDayComboBox.setClearButtonVisible(true);
        workingDayComboBox.setMinWidth("23em");

        emergencyHourComboBox.setItems(clinicService.getEmergencyHours());
        emergencyHourComboBox.setItemLabelGenerator(emergencyHourDto ->
                emergencyHourDto.getDay() + ": " + emergencyHourDto.getHour());
        emergencyHourComboBox.setClearButtonVisible(true);
        emergencyHourComboBox.setMinWidth("18em");


        binder.bindInstanceFields(this);

        specialization.setItems(Specialization.values());

        clearForm();

        //ADD and REMOVE WORKING_DAY_Button + CANCEL_Button
        addDayButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        removeDayButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        HorizontalLayout daysModButtons = new HorizontalLayout();
        daysModButtons.add(addDayButton, removeDayButton);
        addDayButton.addClickListener(event -> addWorkingDayToDoctorSchedule());
        removeDayButton.addClickListener(event -> removeWorkingDayFromDoctorSchedule());

        //day mod layout
        VerticalLayout workingDaysModLayout = new VerticalLayout();
        workingDaysModLayout.add(workingDayComboBox, daysModButtons);

        //ADD and REMOVE EMERGENCY_HOUR BUTTON
        addEmHourButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        removeEmHourButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        HorizontalLayout emHoursModButtons = new HorizontalLayout();
        emHoursModButtons.add(addEmHourButton, removeEmHourButton);
        addEmHourButton.addClickListener(event -> addEmergencyHourToDoctorSchedule());
        removeEmHourButton.addClickListener(event -> removeEmergencyHourFromDoctorSchedule());

        //CANCEL Button
        cancelButton.addClickListener(event -> this.setVisible(false));

        //hour mod layout
        VerticalLayout emHoursModLayout = new VerticalLayout();
        emHoursModLayout.add(emergencyHourComboBox, emHoursModButtons);

        //day+hour mod layout
        VerticalLayout modLayout = new VerticalLayout();
        modLayout.add(workingDaysModLayout, emHoursModLayout, cancelButton);
        modLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, cancelButton);

        VerticalLayout formLayout = new VerticalLayout(name, surname, specialization, email, schedule);
        schedule.setMinWidth("25em");

        HorizontalLayout formModLayout = new HorizontalLayout(formLayout, modLayout);
        formModLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.AUTO);

        add(formModLayout);
    }

    private void addWorkingDayToDoctorSchedule() {
        DoctorDto doctorDto = binder.getBean();

        List<WorkingDayDto> docAllWorkingDays =
                doctorDto.getClinicDoctorScheduleDto().getWorkingDaysDtos();
        WorkingDayDto workingDayDto;

        if (!workingDayComboBox.isEmpty()) {
            workingDayDto = workingDayComboBox.getValue();

            if (!docAllWorkingDays.contains(workingDayDto)) {
                docAllWorkingDays.add(workingDayDto);
                doctorDto.getClinicDoctorScheduleDto().setWorkingDaysDtos(docAllWorkingDays);
                clinicService.saveDoctor(doctorDto);
            }
        }

        initScheduleData();
        this.setVisible(true);
    }

    private void removeWorkingDayFromDoctorSchedule() {
        DoctorDto doctorDto = binder.getBean();

        List<WorkingDayDto> workingDaysDtos =
                doctorDto.getClinicDoctorScheduleDto().getWorkingDaysDtos();
        WorkingDayDto workingDayToRemove;

        if (!workingDayComboBox.isEmpty()) {
            workingDayToRemove = workingDayComboBox.getValue();
            workingDaysDtos.removeIf(workingDay -> workingDay.equals(workingDayToRemove));
            doctorDto.getClinicDoctorScheduleDto().setWorkingDaysDtos(workingDaysDtos);
            clinicService.saveDoctor(doctorDto);
        }

        initScheduleData();
        this.setVisible(true);
    }

    private void addEmergencyHourToDoctorSchedule() {
        DoctorDto doctorDto = binder.getBean();

        List<EmergencyHourDto> docAllEmergencyHours =
                doctorDto.getClinicDoctorScheduleDto().getEmergencyHoursDtos();
        EmergencyHourDto emergencyHourDto;

        if (!emergencyHourComboBox.isEmpty()) {
            emergencyHourDto = emergencyHourComboBox.getValue();

            if (!docAllEmergencyHours.contains(emergencyHourDto)) {
                docAllEmergencyHours.add(emergencyHourDto);
                doctorDto.getClinicDoctorScheduleDto().setEmergencyHoursDtos(docAllEmergencyHours);
                clinicService.saveDoctor(doctorDto);
            }
        }

        initScheduleData();
        this.setVisible(true);
    }

    private void removeEmergencyHourFromDoctorSchedule() {
        DoctorDto doctorDto = binder.getBean();

        List<EmergencyHourDto> docAllEmergencyHours =
                doctorDto.getClinicDoctorScheduleDto().getEmergencyHoursDtos();
        EmergencyHourDto emergencyHourDtoToRemove;

        if (!emergencyHourComboBox.isEmpty()) {
            emergencyHourDtoToRemove = emergencyHourComboBox.getValue();
            docAllEmergencyHours.removeIf(emergencyHour -> emergencyHour.equals(emergencyHourDtoToRemove));
            doctorDto.getClinicDoctorScheduleDto().setEmergencyHoursDtos(docAllEmergencyHours);

            clinicService.saveDoctor(doctorDto);
        }

        initScheduleData();
        this.setVisible(true);
    }

    public void initScheduleData() {
        DoctorDto doctorDto = binder.getBean();
        schedule.setValue(doctorDto.getClinicDoctorScheduleDto().toString()
                .replace(",", "")
                .replace("[", "")
                .replace("]", "")
                .trim());
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
