package com.kodilla.clinic.ui.views.patient;

import com.kodilla.clinic.backend.enums.Day;
import com.kodilla.clinic.backend.enums.Specialization;
import com.kodilla.clinic.backend.enums.Status;
import com.kodilla.clinic.backend.enums.Visits;
import com.kodilla.clinic.backend.outerapi.dtos.AppointmentDto;
import com.kodilla.clinic.backend.outerapi.dtos.DoctorDto;
import com.kodilla.clinic.backend.outerapi.dtos.PatientDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "bookDoctorAppointment", layout = MainLayout.class)
@PageTitle("Book appointment | Clinic App")
public class BookAppointmentFormView extends FormLayout {

    //LOGIN
    private BigDecimalField peselField = new BigDecimalField("Please enter your PESEL number");
    private Button signInButton = new Button("Sign in");

    //DECISION PREPARATION
    private TextField greetingTextField = new TextField("YOU ARE SIGNED IN!");
    private Button myAppointmentsButton = new Button("My appointments");

    private ComboBox<Specialization> chooseSpecializationComboBox = new ComboBox<>("Choose specialization");
    private ComboBox<DoctorDto> chooseDoctorComboBox = new ComboBox<>("Choose doctor");

    private Button searchAppointmentsButton = new Button("Search appointments");

    private VerticalLayout decisionPreparationLayout_LowerPart;

    //BOOKING
    private ComboBox<Day> chooseWeekdayComboBox = new ComboBox<>("Choose weekday");
    private ComboBox<AppointmentDto> chooseAppointmentComboBox = new ComboBox<>("Choose appointment");
    private Button reserveButton = new Button("Reserve");

    private VerticalLayout bookingLayout;

    //MY VISITS GRID
    private ComboBox<Visits> filterMyAppointmentsByTime = new ComboBox<>("Filter by visit time");
    private Grid<AppointmentDto> patientScheduledAppointmentsGrid = new Grid<>(AppointmentDto.class);

    private TextField patientAppointmentToCancelTextField = new TextField("Visit");
    private Button cancelAppointmentButton = new Button("Cancel this visit");

    private Button hidePatientAppointmentsButton = new Button("Hide");

    private VerticalLayout patientScheduledAppointmentsLayout;

    //DOCTOR APPOINTMENTS GRID
    private Grid<AppointmentDto> doctorAppointmentsGrid = new Grid<>(AppointmentDto.class);
    private Button hideDoctorsAppointmentsButton = new Button("Hide");

    private VerticalLayout doctorAppointmentsLayout;

    //SERVICE
    private ClinicService clinicService;
    private PatientDto currPatient;

    public BookAppointmentFormView(ClinicService clinicService) {
        this.clinicService = clinicService;

        //LOGIN
        peselField.setMinWidth("13em");

        signInButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        signInButton.addClickListener(event -> signIn());

        //DECISION PREPARATION
        greetingTextField.setReadOnly(true);

        chooseSpecializationComboBox.setItems(Specialization.values());
        chooseSpecializationComboBox.addValueChangeListener(event -> updateChooseDoctorCombobox());
        chooseSpecializationComboBox.setClearButtonVisible(true);

        chooseDoctorComboBox.setItems(clinicService.getDoctors());
        chooseDoctorComboBox.setItemLabelGenerator(doctorDto ->
                doctorDto.getName() + " " + doctorDto.getSurname() + " M.D. " +
                        "Specialization: " + doctorDto.getSpecialization());
        chooseDoctorComboBox.addValueChangeListener(event -> updateChooseAppointmentCombobox());
        chooseDoctorComboBox.setClearButtonVisible(true);

        myAppointmentsButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        myAppointmentsButton.addClickListener(event -> setVisible_myScheduledAppointmentsLayout());

        //LAYOUTS
                //SIGN IN
        VerticalLayout peselSignInLayout = new VerticalLayout(peselField, signInButton);

                //GREETING
        HorizontalLayout greetingMyAppointmentsLayout = new HorizontalLayout(greetingTextField, myAppointmentsButton);
        greetingMyAppointmentsLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

                //DECISION PREPARATION
        decisionPreparationLayout_LowerPart = new VerticalLayout(
                greetingMyAppointmentsLayout,
                chooseSpecializationComboBox,
                chooseDoctorComboBox,
                searchAppointmentsButton);
        decisionPreparationLayout_LowerPart.setVisible(false);

                //RESERVATION
        bookingLayout = reservationLayout();
        bookingLayout.setVisible(false);

                //DOCTOR APPOINTMENTS GRID
        doctorAppointmentsLayout = getDoctorAppointmentsLayout();
        doctorAppointmentsLayout.setVisible(false);
        doctorAppointmentsLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

                    //FIRST_STEPS: SIGN IN + GREETING + DECISION PREPARATION + BOOKING
        VerticalLayout firstStepsLayout = new VerticalLayout(
                peselSignInLayout, decisionPreparationLayout_LowerPart, bookingLayout);

                //SCHEDULED APPOINTMENTS GRID
        patientScheduledAppointmentsLayout = getPatientScheduledAppointmentsLayout();
        patientScheduledAppointmentsLayout.setVisible(false);


                    //FIRST_STEPS + SCHEDULED APPOINTMENTS GRID
        HorizontalLayout formGridLayout = new HorizontalLayout(
                firstStepsLayout, patientScheduledAppointmentsLayout);

        //SEARCH APPOINTMENTS BUTTON
        searchAppointmentsButton.addClickListener(event -> {
            if (!chooseDoctorComboBox.isEmpty()) {
                bookingLayout.setVisible(true);
                doctorAppointmentsLayout.setVisible(true);
                doctorAppointmentsGrid.setItems(
                        clinicService.getAppointments_ByDoctorId(chooseDoctorComboBox.getValue().getDoctor_id()));
            }
        });

        VerticalLayout endLayout = new VerticalLayout(formGridLayout, doctorAppointmentsLayout);
        endLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.START);

        add(endLayout);
    }

    private VerticalLayout reservationLayout() {
        VerticalLayout reservationLayout = new VerticalLayout();

        chooseWeekdayComboBox.setItems(Day.values());
        chooseWeekdayComboBox.addValueChangeListener(event -> updateChooseAppointmentCombobox());
        chooseWeekdayComboBox.setClearButtonVisible(true);

        chooseAppointmentComboBox.setItemLabelGenerator(appointmentDto -> {
            LocalDateTime visitTime = appointmentDto.getDateTime();
            return "Status: " + appointmentDto.getStatus() +
                    " \nDate: " + visitTime.getDayOfMonth() + " " + visitTime.getMonth() + " " + visitTime.getYear() +
                    " \n" + visitTime.getHour() + ":" + (visitTime.getMinute() >= 10 ? visitTime.getMinute() : "0" + visitTime.getMinute());
        });
        chooseAppointmentComboBox.setClearButtonVisible(true);
        chooseAppointmentComboBox.setMinWidth("26em");

        reserveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        reserveButton.addClickListener(event -> makeReservation());

        reservationLayout.add(chooseWeekdayComboBox, chooseAppointmentComboBox, reserveButton);
        return reservationLayout;
    }

    private void updateChooseAppointmentCombobox() {
        List<AppointmentDto> docVisits = new ArrayList<>();

        if (!chooseDoctorComboBox.isEmpty()) {
            docVisits = clinicService.getAppointments_ByDoctorId(
                    chooseDoctorComboBox.getValue().getDoctor_id()).stream()
                    .filter(appointmentDto -> appointmentDto.getStatus().equals(Status.OPEN))
                    .collect(Collectors.toList());
        }

        if (!chooseWeekdayComboBox.isEmpty()) {
            DayOfWeek dayOfWeek = dayToDayOfWeekConverter(chooseWeekdayComboBox.getValue());
            docVisits = docVisits.stream()
                    .filter(appointmentDto -> appointmentDto.getDateTime().getDayOfWeek()
                            .equals(dayOfWeek))
                    .collect(Collectors.toList());
        }

        chooseAppointmentComboBox.setItems(docVisits);
    }

    private DayOfWeek dayToDayOfWeekConverter(Day value) {
        switch (value) {
            case MONDAY:
                return DayOfWeek.MONDAY;
            case TUESDAY:
                return DayOfWeek.TUESDAY;
            case WEDNESDAY:
                return DayOfWeek.WEDNESDAY;
            case THURSDAY:
                return DayOfWeek.THURSDAY;
            case FRIDAY:
                return DayOfWeek.FRIDAY;
        }
        return DayOfWeek.SATURDAY;
    }

    private void setVisible_myScheduledAppointmentsLayout() {
        patientScheduledAppointmentsLayout.setVisible(true);
    }

    private void updateChooseDoctorCombobox() {
        if (chooseSpecializationComboBox.isEmpty()) {
            chooseDoctorComboBox.setItems(clinicService.getDoctors());
        } else if (!chooseSpecializationComboBox.isEmpty()) {
            chooseDoctorComboBox.setItems(clinicService.getDoctors_BySpecialization(chooseSpecializationComboBox.getValue()));
        }
    }

    private void signIn() {
        List<PatientDto> patientDtos = clinicService.getPatients();
        List<BigDecimal> pesels = patientDtos.stream()
                .map(PatientDto::getPesel)
                .collect(Collectors.toList());

        BigDecimal patientsPesel = BigDecimal.ZERO;
        if (!peselField.isEmpty()) {
            patientsPesel = peselField.getValue();
        }

        if (pesels.contains(patientsPesel)) {
            currPatient = clinicService.getPatient_ByPesel(patientsPesel);

            decisionPreparationLayout_LowerPart.setVisible(true);
            greetingTextField.setValue("Hi " + currPatient.getName() + " " + currPatient.getSurname() + "!");

            patientScheduledAppointmentsGrid.setItems(
                    clinicService.getAppointments_ByPatientId(currPatient.getPatient_id()));
        }
    }

    private void makeReservation() {
        AppointmentDto chosenAppointment;

        if (!chooseAppointmentComboBox.isEmpty()) {
            chosenAppointment = chooseAppointmentComboBox.getValue();
            chosenAppointment.setStatus(Status.RESERVED);
            chosenAppointment.setPatientId(currPatient.getPatient_id());
            clinicService.saveAppointment(chosenAppointment);
        }
        updateChooseAppointmentCombobox();
        refreshGrid_PatientScheduledAppointments();
        refreshGrid_ChosenDoctorAppointments();
    }

    private VerticalLayout getDoctorAppointmentsLayout() {
        VerticalLayout doctorAppointmentsLayout = new VerticalLayout();

        hideDoctorsAppointmentsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        hideDoctorsAppointmentsButton.addClickListener(event -> this.doctorAppointmentsLayout.setVisible(false));

        doctorAppointmentsGrid.setColumns("status", "dateTime");
        doctorAppointmentsGrid.addColumn(appointmentDto ->
                "Dr " + clinicService.getDoctorById(appointmentDto.getDoctorId()).getSurname() + " | " +
                        clinicService.getDoctorById(appointmentDto.getDoctorId()).getSpecialization());

        setColumnNames_ScheduledAppointments(doctorAppointmentsGrid);

        doctorAppointmentsGrid.asSingleSelect()
                .addValueChangeListener(
                        event -> patientScheduledAppointmentsGrid.getSelectionModel().deselectAll());

        doctorAppointmentsGrid.setMinWidth("90em");

        doctorAppointmentsLayout.add(hideDoctorsAppointmentsButton, doctorAppointmentsGrid);
        return doctorAppointmentsLayout;
    }

    private VerticalLayout getPatientScheduledAppointmentsLayout() {
        VerticalLayout myScheduledAppointmentsLayout = new VerticalLayout();

        patientAppointmentToCancelTextField.setReadOnly(true);

        hidePatientAppointmentsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        hidePatientAppointmentsButton.addClickListener(event -> myScheduledAppointmentsLayout.setVisible(false));

        cancelAppointmentButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelAppointmentButton.addClickListener(event -> cancelAppointment());

        filterMyAppointmentsByTime.setPlaceholder("Past, Forthcoming...");
        filterMyAppointmentsByTime.setClearButtonVisible(true);
        filterMyAppointmentsByTime.addValueChangeListener(e -> updatePatientScheduledAppointments());
        filterMyAppointmentsByTime.setItems(Visits.values());

        VerticalLayout filter_Hide = new VerticalLayout(filterMyAppointmentsByTime, hidePatientAppointmentsButton);
        VerticalLayout thisAppointment_Cancel = new VerticalLayout(patientAppointmentToCancelTextField, cancelAppointmentButton);

        HorizontalLayout layoutTop = new HorizontalLayout(filter_Hide, thisAppointment_Cancel);
        thisAppointment_Cancel.setHorizontalComponentAlignment(FlexComponent.Alignment.BASELINE);

        patientScheduledAppointmentsGrid.setColumns("status", "dateTime");
        patientScheduledAppointmentsGrid.addColumn(appointmentDto ->
                "Patient: " + clinicService.getPatientById(appointmentDto.getPatientId()).getName()
                        + " " + clinicService.getPatientById(appointmentDto.getPatientId()).getSurname());
        patientScheduledAppointmentsGrid.addColumn(appointmentDto ->
                "Dr " + clinicService.getDoctorById(appointmentDto.getDoctorId()).getSurname() + " | " +
                        clinicService.getDoctorById(appointmentDto.getDoctorId()).getSpecialization());

        setColumnNames_ScheduledAppointments(patientScheduledAppointmentsGrid);

        patientScheduledAppointmentsGrid.asSingleSelect()
                .addValueChangeListener(
                        event -> {
                            AppointmentDto gridCurrVal = patientScheduledAppointmentsGrid.asSingleSelect().getValue();
                            if (!patientScheduledAppointmentsGrid.asSingleSelect().isEmpty()) {
                                patientAppointmentToCancelTextField.setValue(
                                        "Dr " + clinicService.getDoctorById(gridCurrVal.getDoctorId()).getSurname() + " | Specialization: " +
                                                clinicService.getDoctorById(gridCurrVal.getDoctorId()).getSpecialization() +
                                                " | Visit time: " + gridCurrVal.getDateTime());
                            }
                        });
        patientAppointmentToCancelTextField.setMinWidth("40em");

        patientScheduledAppointmentsGrid.setMinWidth("90em");

        myScheduledAppointmentsLayout.add(layoutTop, patientScheduledAppointmentsGrid);
        return myScheduledAppointmentsLayout;
    }

    private void cancelAppointment() {
        AppointmentDto appointmentDto = patientScheduledAppointmentsGrid.asSingleSelect().getValue();
        patientAppointmentToCancelTextField.clear();
        patientScheduledAppointmentsGrid.deselectAll();

        if (appointmentDto.getStatus().equals(Status.RESERVED)) {
            appointmentDto.setPatientId(0);
            appointmentDto.setStatus(Status.OPEN);
            clinicService.saveAppointment(appointmentDto);
        }
        refreshGrid_PatientScheduledAppointments();
        refreshGrid_ChosenDoctorAppointments();
        updateChooseAppointmentCombobox();
    }

    private void updatePatientScheduledAppointments() {
        if (filterMyAppointmentsByTime.isEmpty()) {
            refreshGrid_PatientScheduledAppointments();
        } else {
            patientScheduledAppointmentsGrid.setItems(
                    clinicService.getAppointments_ByStatus_And_ByPatientId(
                            currPatient.getPatient_id(), filterMyAppointmentsByTime.getValue()));
        }
    }

    private void refreshGrid_ChosenDoctorAppointments() {
        if (!chooseDoctorComboBox.isEmpty()) {
            doctorAppointmentsGrid.getDataProvider().refreshAll();
            doctorAppointmentsGrid.setItems(
                    clinicService.getAppointments_ByDoctorId(chooseDoctorComboBox.getValue().getDoctor_id()));
        }

    }
    public void refreshGrid_PatientScheduledAppointments() {
        patientScheduledAppointmentsGrid.getDataProvider().refreshAll();
        patientScheduledAppointmentsGrid.setItems(
                clinicService.getAppointments_ByPatientId(currPatient.getPatient_id()));
    }

    private void setColumnNames_ScheduledAppointments(Grid<AppointmentDto> grid) {
        String propertyId1 = "status";
        Grid.Column<AppointmentDto> col1 = grid.getColumnByKey(propertyId1);

        if (col1 != null) {
            col1.setHeader("Visit status");
        }
    }
}
