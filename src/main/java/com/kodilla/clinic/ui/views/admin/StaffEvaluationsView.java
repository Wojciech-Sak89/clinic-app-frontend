package com.kodilla.clinic.ui.views.admin;

import com.kodilla.clinic.backend.enums.Stars;
import com.kodilla.clinic.backend.outerapi.dtos.StaffEvaluationDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.MainLayout;
import com.kodilla.clinic.ui.views.forms.StaffEvaluationForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "staffEvaluations", layout = MainLayout.class)
@PageTitle("Staff evaluations | Clinic App")
public class StaffEvaluationsView extends VerticalLayout {
    private ClinicService clinicService;
    private StaffEvaluationForm staffEvaluationForm;
    private Button addEvaluationButton = new Button("Add evaluation");

    private ComboBox<Stars> filter = new ComboBox<>("Filter by rating");

    private Grid<StaffEvaluationDto> staffEvaluationGrid = new Grid<>(StaffEvaluationDto.class);

    public StaffEvaluationsView(ClinicService clinicService) {
        this.clinicService = clinicService;
        staffEvaluationForm = new StaffEvaluationForm(this, clinicService);

        staffEvaluationGrid.setColumns("patient_Id", "doctor_Id", "stars", "opinion", "entryDate");
        setColumnNames(staffEvaluationGrid);

        filter.setPlaceholder("One, Two stars...");
        filter.setClearButtonVisible(true);
        filter.addValueChangeListener(e -> update());
        filter.setItems(Stars.values());

        addEvaluationButton.addClickListener(e -> {
            staffEvaluationGrid.asSingleSelect().clear();
            staffEvaluationForm.setStaffEvaluationDto(new StaffEvaluationDto());
        });

        HorizontalLayout toolbar = new HorizontalLayout(filter, addEvaluationButton);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.END);

        HorizontalLayout mainContent = new HorizontalLayout(staffEvaluationGrid, staffEvaluationForm);
        mainContent.setSizeFull();

        staffEvaluationForm.setStaffEvaluationDto(null);

        add(toolbar, mainContent);
        setSizeFull();
        refresh();

        staffEvaluationGrid.asSingleSelect()
                .addValueChangeListener(
                        event -> staffEvaluationForm.setStaffEvaluationDto(staffEvaluationGrid.asSingleSelect().getValue()));
    }

    private void update() {
        if(filter.isEmpty()) {
            refresh();
        } else {
            staffEvaluationGrid.setItems(clinicService.getEvaluations_ByStarsRating(filter.getValue()));
        }
    }

    private void setColumnNames(Grid<StaffEvaluationDto> grid) {
        String propertyId1 = "stars";
        Grid.Column<StaffEvaluationDto> col1 = grid.getColumnByKey(propertyId1);

        if (col1 != null) {
            col1.setHeader("Rating (1-5)");
        }
    }

    public void refresh() {
        staffEvaluationGrid.getDataProvider().refreshAll();
        staffEvaluationGrid.setItems(clinicService.getStaffEvaluations());
    }
}
