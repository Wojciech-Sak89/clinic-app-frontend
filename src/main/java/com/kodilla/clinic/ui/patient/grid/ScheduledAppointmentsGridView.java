package com.kodilla.clinic.ui.patient.grid;

import com.kodilla.clinic.backend.enums.Visits;
import com.kodilla.clinic.backend.outerapi.dtos.AppointmentDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.patient.BookAppointmentFormView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;

import java.time.format.DateTimeFormatter;

public class ScheduledAppointmentsGridView extends VerticalLayout {
    //MY VISITS GRID
    private ComboBox<Visits> filterMyAppointmentsByTime;
    private Grid<AppointmentDto> patientScheduledAppointmentsGrid;

    private TextField patientAppointmentToCancelTextField;
    private Button cancelAppointmentButton = new Button("Cancel this visit");

    private Button hidePatientAppointmentsButton = new Button("Hide");

    //SERVICE
    private ClinicService clinicService;
    private BookAppointmentFormView bookAppointmentFormView;

    public ScheduledAppointmentsGridView(ComboBox<Visits> filterMyAppointmentsByTime,
                                         Grid<AppointmentDto> patientScheduledAppointmentsGrid,
                                         TextField patientAppointmentToCancelTextField,
                                         ClinicService clinicService,
                                         BookAppointmentFormView bookAppointmentFormView) {
        this.filterMyAppointmentsByTime = filterMyAppointmentsByTime;
        this.patientScheduledAppointmentsGrid = patientScheduledAppointmentsGrid;
        this.patientAppointmentToCancelTextField = patientAppointmentToCancelTextField;
        this.clinicService = clinicService;
        this.bookAppointmentFormView = bookAppointmentFormView;

        VerticalLayout myScheduledAppointmentsLayout = new VerticalLayout();

        this.patientAppointmentToCancelTextField.setReadOnly(true);

        hidePatientAppointmentsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        hidePatientAppointmentsButton.addClickListener(event -> myScheduledAppointmentsLayout.setVisible(false));

        cancelAppointmentButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelAppointmentButton.addClickListener(event -> this.bookAppointmentFormView.cancelAppointment());

        this.filterMyAppointmentsByTime.setPlaceholder("Past, Forthcoming...");
        this.filterMyAppointmentsByTime.setClearButtonVisible(true);
        this.filterMyAppointmentsByTime.addValueChangeListener(
                e -> this.bookAppointmentFormView.updatePatientScheduledAppointments());
        this.filterMyAppointmentsByTime.setItems(Visits.values());

        VerticalLayout filter_Hide = new VerticalLayout(filterMyAppointmentsByTime, hidePatientAppointmentsButton);
        VerticalLayout thisAppointment_Cancel = new VerticalLayout(patientAppointmentToCancelTextField, cancelAppointmentButton);

        HorizontalLayout layoutTop = new HorizontalLayout(filter_Hide, thisAppointment_Cancel);
        thisAppointment_Cancel.setHorizontalComponentAlignment(FlexComponent.Alignment.BASELINE);

        this.patientScheduledAppointmentsGrid.setColumns("status");
        this.patientScheduledAppointmentsGrid.addColumn(new LocalDateTimeRenderer<>(
                AppointmentDto::getDateTime,
                "dd.MM.yyyy HH:mm")
        ).setHeader("Visit time");
        this.patientScheduledAppointmentsGrid.addColumn(appointmentDto ->
                this.clinicService.getPatientById(appointmentDto.getPatientId()).getName()
                        + " " + this.clinicService.getPatientById(appointmentDto.getPatientId()).getSurname())
                .setHeader("Patient");
        this.patientScheduledAppointmentsGrid.addColumn(appointmentDto ->
                this.clinicService.getDoctorById(appointmentDto.getDoctorId()).getSurname() + " | " +
                        this.clinicService.getDoctorById(appointmentDto.getDoctorId()).getSpecialization())
                .setHeader("Doctor");

        this.bookAppointmentFormView.setColumnNames_ScheduledAppointments(patientScheduledAppointmentsGrid);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        patientScheduledAppointmentsGrid.asSingleSelect()
                .addValueChangeListener(
                        event -> {
                            AppointmentDto gridCurrVal = patientScheduledAppointmentsGrid.asSingleSelect().getValue();
                            if (!this.patientScheduledAppointmentsGrid.asSingleSelect().isEmpty()) {
                                this.patientAppointmentToCancelTextField.setValue(
                                        "Dr " + this.clinicService.getDoctorById(gridCurrVal.getDoctorId()).getSurname() + " | Specialization: " +
                                                this.clinicService.getDoctorById(gridCurrVal.getDoctorId()).getSpecialization() +
                                                " | Visit time: " + gridCurrVal.getDateTime().format(formatter));
                            }
                        });
        this.patientAppointmentToCancelTextField.setMinWidth("40em");

        this.patientScheduledAppointmentsGrid.setMinWidth("90em");

        myScheduledAppointmentsLayout.add(layoutTop, patientScheduledAppointmentsGrid);

        add(myScheduledAppointmentsLayout);
    }
}
