package com.kodilla.clinic.ui.views;

import com.kodilla.clinic.backend.outerapi.dtos.AppointmentDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.MainLayout;
import com.kodilla.clinic.ui.views.forms.AppointmentsForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "appointments", layout = MainLayout.class)
@PageTitle("Appointments | Clinic App")
public class AppointmentsView extends VerticalLayout {
    private ClinicService clinicService;
    private AppointmentsForm appointmentsForm;
    private Button addAppointmentButton = new Button("Add appointment");

    private Grid<AppointmentDto> appointmentGrid = new Grid<>(AppointmentDto.class);

    public AppointmentsView(ClinicService clinicService) {
        this.clinicService = clinicService;
        appointmentsForm = new AppointmentsForm(this, clinicService);

        appointmentGrid.setColumns("patientId", "doctorId", "status", "forEmergency", "dateTime");
        setColumnNames(appointmentGrid);

        addAppointmentButton.addClickListener(e -> {
            appointmentGrid.asSingleSelect().clear();
            appointmentsForm.setAppointmentDto(new AppointmentDto());
        });

        HorizontalLayout toolbar = new HorizontalLayout(addAppointmentButton);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.END);

        HorizontalLayout mainContent = new HorizontalLayout(appointmentGrid, appointmentsForm);
        mainContent.setSizeFull();

        appointmentsForm.setAppointmentDto(null);

        add(toolbar, mainContent);
        setSizeFull();
        refresh();

        appointmentGrid.asSingleSelect()
                .addValueChangeListener(
                        event -> appointmentsForm.setAppointmentDto(appointmentGrid.asSingleSelect().getValue()));
    }

    private void setColumnNames(Grid<AppointmentDto> grid) {
        String propertyId1 = "forEmergency";
        Grid.Column<AppointmentDto> col1 = grid.getColumnByKey(propertyId1);

        if (col1 != null) {
            col1.setHeader("For patients in urgent state");
        }
    }

    public void refresh() {
        appointmentGrid.getDataProvider().refreshAll();
        appointmentGrid.setItems(clinicService.getAppointments());
    }
}
