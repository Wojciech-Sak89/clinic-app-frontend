package com.kodilla.clinic.ui.views;

import com.kodilla.clinic.backend.outerapi.dtos.PatientDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.MainLayout;
import com.kodilla.clinic.ui.views.forms.PatientForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "patientsRegistration", layout = MainLayout.class)
@PageTitle("Patients | Clinic App")
public class PatientsView extends VerticalLayout {
    private ClinicService clinicService;
    private PatientForm patientForm;
    private Button addPatientButton = new Button("Add patient");

    private Grid<PatientDto> patientGrid = new Grid<>(PatientDto.class);
    private TextField filter = new TextField("Filter by surname");

    public PatientsView(ClinicService clinicService) {
        this.clinicService = clinicService;
        patientForm = new PatientForm(this, clinicService);

        patientGrid.setColumns(
                "patient_id", "name", "surname", "address", "birthDate", "pesel", "telNum", "email", "inUrgency");
        setColumnNames(patientGrid);

        filter.setPlaceholder("surname...");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> update());

        addPatientButton.addClickListener(e -> {
            patientGrid.asSingleSelect().clear();
            patientForm.setPatientDto(new PatientDto());
        });

        HorizontalLayout toolbar = new HorizontalLayout(filter, addPatientButton);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.END);

        HorizontalLayout mainContent = new HorizontalLayout(patientGrid, patientForm);
        mainContent.setSizeFull();

        patientForm.setPatientDto(null);

        add(toolbar, mainContent);
        setSizeFull();
        refresh();

        patientGrid.asSingleSelect()
                .addValueChangeListener(
                        event -> patientForm.setPatientDto(patientGrid.asSingleSelect().getValue()));
    }

    private void update() {
        if(filter.isEmpty()) {
            refresh();
        } else {
            patientGrid.setItems(clinicService.getPatients_BySurname(filter.getValue()));
        }
    }

    private void setColumnNames(Grid<PatientDto> grid) {
        String propertyId1 = "telNum";
        String propertyId2 = "inUrgency";
        Grid.Column<PatientDto> col1 = grid.getColumnByKey(propertyId1);
        Grid.Column<PatientDto> col2 = grid.getColumnByKey(propertyId2);

        if (col1 != null && col2 != null) {
            col1.setHeader("Tel. number");
            col2.setHeader("Patient in urgency");
        }
    }

    public void refresh() {
        patientGrid.getDataProvider().refreshAll();
        patientGrid.setItems(clinicService.getPatients());
    }

}
