package com.kodilla.clinic.ui.views.admin;

import com.kodilla.clinic.backend.enums.Day;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.EmergencyHourDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.MainLayout;
import com.kodilla.clinic.ui.views.forms.EmergencyHourForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "emergencyHours", layout = MainLayout.class)
@PageTitle("Emergency Hours | Clinic App")
public class EmergencyHoursView extends VerticalLayout {
    private ClinicService clinicService;
    private EmergencyHourForm emergencyHourForm;
    private Button addEmHourButton = new Button("Add new emergency hour");

    private Grid<EmergencyHourDto> emergencyHourGrid = new Grid<>(EmergencyHourDto.class);
    private ComboBox<Day> filter = new ComboBox<>("Filter by weekday");

    public EmergencyHoursView(ClinicService clinicService) {
        this.clinicService = clinicService;
        emergencyHourForm = new EmergencyHourForm(this, clinicService);

        emergencyHourGrid.setColumns("day", "hour");
        setColumnNames(emergencyHourGrid);

        filter.setPlaceholder("Moday, Tuesday...");
        filter.setClearButtonVisible(true);
        filter.addValueChangeListener(e -> update());
        filter.setItems(Day.values());

        addEmHourButton.addClickListener(e -> {
            emergencyHourGrid.asSingleSelect().clear(); //???
            emergencyHourForm.setEmergencyHourDto(new EmergencyHourDto());
        });

        HorizontalLayout toolbar = new HorizontalLayout(filter, addEmHourButton);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.END);

        HorizontalLayout mainContent = new HorizontalLayout(emergencyHourGrid, emergencyHourForm);
        mainContent.setSizeFull();

        emergencyHourForm.setEmergencyHourDto(null);

        add(toolbar, mainContent);
        setSizeFull();
        refresh();

        emergencyHourGrid.asSingleSelect()
                .addValueChangeListener(
                        event -> emergencyHourForm.setEmergencyHourDto(emergencyHourGrid.asSingleSelect().getValue()));
    }

    private void update() {
        if(filter.isEmpty()) {
            refresh();
        } else {
            emergencyHourGrid.setItems(clinicService.getEmergencyHours_ByWeekday(filter.getValue()));
        }
    }

    private void setColumnNames(Grid<EmergencyHourDto> grid) {
        String propertyId1 = "day";
        String propertyId2 = "hour";
        Grid.Column<EmergencyHourDto> col1 = grid.getColumnByKey(propertyId1);
        Grid.Column<EmergencyHourDto> col2 = grid.getColumnByKey(propertyId2);

        if (col1 != null && col2 != null) {
            col1.setHeader("Weekday");
            col2.setHeader("Emergency hour");
        }
    }

    public void refresh() {
        emergencyHourGrid.getDataProvider().refreshAll();
        emergencyHourGrid.setItems(clinicService.getEmergencyHours());
    }
}
