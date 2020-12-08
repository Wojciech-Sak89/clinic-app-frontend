package com.kodilla.clinic.ui.admin.view;

import com.kodilla.clinic.backend.enums.Specialization;
import com.kodilla.clinic.backend.outerapi.dtos.DoctorDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.MainLayout;
import com.kodilla.clinic.ui.admin.form.DoctorScheduleForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "doctorsSchedules", layout = MainLayout.class)
@PageTitle("Doctors Schedules | Clinic App")
public class DoctorScheduleView extends VerticalLayout {
    private ClinicService clinicService;
    private DoctorScheduleForm doctorScheduleForm;
    private Button addScheduleButton = new Button("Modify schedule");

    private Grid<DoctorDto> doctorGrid = new Grid<>(DoctorDto.class);
    private ComboBox<Specialization> filter = new ComboBox<>("Filter by specialization");

    public DoctorScheduleView(ClinicService clinicService) {
        this.clinicService = clinicService;
        doctorScheduleForm = new DoctorScheduleForm(this, clinicService);

        doctorGrid.setColumns("name", "surname", "specialization", "email");
        setColumnNames(doctorGrid);

        filter.setPlaceholder("Specializations...");
        filter.setClearButtonVisible(true);
        filter.addValueChangeListener(e -> update());
        filter.setItems(Specialization.values());

        addScheduleButton.addClickListener(e -> {
            doctorGrid.asSingleSelect().clear();
            doctorScheduleForm.setDoctorDto(new DoctorDto());
        });

        doctorGrid.asSingleSelect()
                .addValueChangeListener(
                        event -> {
                            doctorScheduleForm.setDoctorDto(doctorGrid.asSingleSelect().getValue());
                            doctorScheduleForm.initScheduleData();
                        });
        doctorGrid.setMaxWidth("65em");

        HorizontalLayout toolbar = new HorizontalLayout(filter, addScheduleButton);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.END);

        HorizontalLayout mainContent = new HorizontalLayout(doctorGrid, doctorScheduleForm);
        mainContent.setSizeFull();

        doctorScheduleForm.setDoctorDto(null);

        add(toolbar, mainContent);
        setSizeFull();
        refresh();
    }

    private void update() {
        if(filter.isEmpty()) {
            refresh();
        } else {
            doctorGrid.setItems(clinicService.getDoctors_BySpecialization(filter.getValue()));
        }
    }

    private void setColumnNames(Grid<DoctorDto> grid) {
        String propertyId1 = "bio";
        Grid.Column<DoctorDto> col1 = grid.getColumnByKey(propertyId1);

        if (col1 != null ) {
            col1.setHeader("Biografical note");
        }
    }

    public void refresh() {
        doctorGrid.getDataProvider().refreshAll();
        doctorGrid.setItems(clinicService.getDoctors());
    }
}
