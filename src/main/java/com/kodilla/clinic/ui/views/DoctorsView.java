package com.kodilla.clinic.ui.views;

import com.kodilla.clinic.backend.enums.Department;
import com.kodilla.clinic.backend.outerapi.dtos.DoctorDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.MainLayout;
import com.kodilla.clinic.ui.views.forms.DoctorForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "doctors", layout = MainLayout.class)
@PageTitle("Doctors | Clinic App")
public class DoctorsView extends VerticalLayout {
    private ClinicService clinicService;
    private DoctorForm doctorForm;
    private Button addDoctorButton = new Button("Add doctor");

    private Grid<DoctorDto> doctorGrid = new Grid<>(DoctorDto.class);
    private ComboBox<Department> filter = new ComboBox<>("Filter by department");

    public DoctorsView(ClinicService clinicService) {
        this.clinicService = clinicService;
        doctorForm = new DoctorForm(this, clinicService);

        doctorGrid.setColumns("name", "surname", "specialization", "department", "email", "bio");
        setColumnNames(doctorGrid);

        filter.setPlaceholder("Departments...");
        filter.setClearButtonVisible(true);
        filter.addValueChangeListener(e -> update());
        filter.setItems(Department.values());

        addDoctorButton.addClickListener(e -> {
            doctorGrid.asSingleSelect().clear();
            doctorForm.setDoctorDto(new DoctorDto());
        });

        HorizontalLayout toolbar = new HorizontalLayout(filter, addDoctorButton);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.END);

        HorizontalLayout mainContent = new HorizontalLayout(doctorGrid, doctorForm);
        mainContent.setSizeFull();

        doctorForm.setDoctorDto(null);

        add(toolbar, mainContent);
        setSizeFull();
        refresh();

        doctorGrid.asSingleSelect()
                .addValueChangeListener(
                        event -> doctorForm.setDoctorDto(doctorGrid.asSingleSelect().getValue()));
    }

    private void update() {
        if(filter.isEmpty()) {
            refresh();
        } else {
            doctorGrid.setItems(clinicService.getDoctors_ByDepartment(filter.getValue()));
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
