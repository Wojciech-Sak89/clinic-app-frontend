package com.kodilla.clinic.ui.views;

import com.kodilla.clinic.backend.enums.Day;
import com.kodilla.clinic.backend.outerapi.dtos.schedule.WorkingDayDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.views.schedule.WorkingDayForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    private ClinicService clinicService;
    private WorkingDayForm workingDayForm;
    private Button addNewWorkingDay = new Button("Add new working day");

    private Grid<WorkingDayDto> workingDayGrid = new Grid<>(WorkingDayDto.class);
    private ComboBox<Day> filter = new ComboBox<>("Filter by weekday");

    public MainView(ClinicService clinicService) {
        this.clinicService = clinicService;
        workingDayForm = new WorkingDayForm(this, clinicService);

        workingDayGrid.setColumns("workingDay_id", "day", "startHour", "endHour", "schedulesIds");
        setColumnNames(workingDayGrid);

        filter.setPlaceholder("Moday, Tuesday...");
        filter.setClearButtonVisible(true);
        filter.addValueChangeListener(e -> update());
        filter.setItems(Day.values());

        addNewWorkingDay.addClickListener(e -> {
            workingDayGrid.asSingleSelect().clear(); //???
            workingDayForm.setWorkingDayDto(new WorkingDayDto());
        });

        HorizontalLayout toolbar = new HorizontalLayout(filter, addNewWorkingDay);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.END);

        HorizontalLayout mainContent = new HorizontalLayout(workingDayGrid, workingDayForm);
        mainContent.setSizeFull();

        workingDayForm.setWorkingDayDto(null);

        add(toolbar, mainContent);
        setSizeFull();
        refresh();

        workingDayGrid.asSingleSelect()
                .addValueChangeListener(
                        event -> workingDayForm.setWorkingDayDto(workingDayGrid.asSingleSelect().getValue()));
    }

    private void update() {
        if(filter.isEmpty()) {
            refresh();
        } else {
            workingDayGrid.setItems(clinicService.getWorkingDays_ByWeekday(filter.getValue()));
        }
    }

    private void setColumnNames(Grid<WorkingDayDto> grid) {
        String propertyId0 = "workingDay_id";
        String propertyId1 = "day";
        String propertyId2 = "startHour";
        String propertyId3 = "endHour";
        String propertyId4 = "schedulesIds";
        Grid.Column<WorkingDayDto> col0 = grid.getColumnByKey(propertyId0);
        Grid.Column<WorkingDayDto> col1 = grid.getColumnByKey(propertyId1);
        Grid.Column<WorkingDayDto> col2 = grid.getColumnByKey(propertyId2);
        Grid.Column<WorkingDayDto> col3 = grid.getColumnByKey(propertyId3);
        Grid.Column<WorkingDayDto> col4 = grid.getColumnByKey(propertyId4);
        if (col0 != null && col1 != null && col2 != null && col3 != null && col4 != null) {
            col0.setHeader("ID (Working Day)");
            col1.setHeader("Weekday");
            col2.setHeader("Start of work time");
            col3.setHeader("End of work time");
            col4.setHeader("Used in (Schedules Ids)");
        }
    }

    public void refresh() {
        workingDayGrid.getDataProvider().refreshAll();
        workingDayGrid.setItems(clinicService.getWorkingDays());
    }
}