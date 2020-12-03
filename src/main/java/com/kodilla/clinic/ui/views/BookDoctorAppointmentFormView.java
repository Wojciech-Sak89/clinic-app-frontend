package com.kodilla.clinic.ui.views;

import com.kodilla.clinic.backend.enums.Day;
import com.kodilla.clinic.backend.enums.Specialization;
import com.kodilla.clinic.backend.outerapi.dtos.AppointmentDto;
import com.kodilla.clinic.backend.outerapi.dtos.DoctorDto;
import com.kodilla.clinic.backend.outerapi.dtos.PatientDto;
import com.kodilla.clinic.backend.outerapi.dtos.StaffEvaluationDto;
import com.kodilla.clinic.backend.service.ClinicService;
import com.kodilla.clinic.ui.MainLayout;
import com.vaadin.flow.component.Component;
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
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "bookDoctorAppointment", layout = MainLayout.class)
@PageTitle("Book doctor appointment | Clinic App")
public class BookDoctorAppointmentFormView extends FormLayout {

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

//    //BOOKING
//    private ComboBox<Day> filterDoctorAppointmentsByWeekDay = new ComboBox<>("Choose weekday");
//    private ComboBox<AppointmentDto> chooseAppointmentComboBox = new ComboBox<>("Choose appointment");
//    private Button reserveButton = new Button("Reserve");

    //MY VISITS GRID
    private ComboBox<Day> filterMyAppointmentsByTime = new ComboBox<>("Filter by visit time");
    private Grid<AppointmentDto> patientScheduledAppointmentsGrid = new Grid<>(AppointmentDto.class);

    private TextField myAppointmentToCancelTextField = new TextField("Visit");
    private Button cancelAppointmentButton = new Button("Cancel this visit");

    private Button hideMyAppointmentsButton = new Button("Hide");

    private VerticalLayout myScheduledAppointmentsLayout;

//    //DOCTOR APPOINTMENTS GRID
//    private Grid<AppointmentDto> doctorAppointmentsGrid = new Grid<>(AppointmentDto.class);
//    private Button hideDoctorsAppointmentsButton = new Button("Hide");

    //UTILS
    private ClinicService clinicService;
    private PatientDto currPatient;

    public BookDoctorAppointmentFormView(ClinicService clinicService) {
        this.clinicService = clinicService;

//        filterMyAppointmentsTime.setPlaceholder("Moday, Tuesday...");
//        filterMyAppointmentsTime.setClearButtonVisible(true);
//        filterMyAppointmentsTime.addValueChangeListener(e -> updateMyAppointments());
//        filterMyAppointmentsTime.setItems(Day.values());
//
//        filterAppointmentsByWeekDay.setPlaceholder("Moday, Tuesday...");
//        filterAppointmentsByWeekDay.setClearButtonVisible(true);
//        filterAppointmentsByWeekDay.addValueChangeListener(e -> updateDoctorsAppointments());
//        filterAppointmentsByWeekDay.setItems(Day.values());

        //LOGIN
        peselField.setMinWidth("13em");

        signInButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        signInButton.addClickListener(event -> signIn());

        //DECISION PREPARATION
        greetingTextField.setReadOnly(true);

        chooseSpecializationComboBox.setItems(Specialization.values());
        chooseSpecializationComboBox.addValueChangeListener(event -> updateChooseDoctorCombobox());

        chooseDoctorComboBox.setItems(clinicService.getDoctors());
        chooseDoctorComboBox.setItemLabelGenerator(doctorDto ->
                doctorDto.getName() + " " + doctorDto.getSurname() + " M.D. " +
                        "Specialization: " + doctorDto.getSpecialization());

        myAppointmentsButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        myAppointmentsButton.addClickListener(event -> setVisible_myScheduledAppointmentsLayout());


//        reserveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        reserveButton.addClickListener(event -> addOpinion());

//        myAppointmentsButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
//        myAppointmentsButton.addClickListener(event -> myScheduledAppointmentsLayout.setVisible(true));

        //LAYOUTS
        VerticalLayout peselSignInLayout = new VerticalLayout(peselField, signInButton);

        HorizontalLayout greetingMyAppointmentsLayout = new HorizontalLayout(greetingTextField, myAppointmentsButton);
        greetingMyAppointmentsLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        decisionPreparationLayout_LowerPart = new VerticalLayout(
                greetingMyAppointmentsLayout,
                chooseSpecializationComboBox,
                chooseDoctorComboBox,
                searchAppointmentsButton);
        decisionPreparationLayout_LowerPart.setVisible(false);


        VerticalLayout fullFormLayout = new VerticalLayout(peselSignInLayout, decisionPreparationLayout_LowerPart);

        myScheduledAppointmentsLayout = myScheduledAppointmentsLayout();
        myScheduledAppointmentsLayout.setVisible(false);
        HorizontalLayout formGridLayout = new HorizontalLayout(fullFormLayout, myScheduledAppointmentsLayout);

        add(formGridLayout);
    }

    private void setVisible_myScheduledAppointmentsLayout() {
        myScheduledAppointmentsLayout.setVisible(true);
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
//        Stars stars = null;
//        String opinion = "";
//        Integer doctor_Id = 0;
//        Integer patient_Id = clinicService.getPatient_ByPesel(
//                peselField.getValue())
//                .getPatient_id();
//
//        if (!chooseSpecializationComboBox.isEmpty()) stars = chooseSpecializationComboBox.getValue();
//        if (!opinionTextArea.isEmpty()) opinion = opinionTextArea.getValue();
//        if (!chooseDoctorComboBox.isEmpty()) doctor_Id = chooseDoctorComboBox.getValue().getDoctor_id();
//
//        StaffEvaluationDto staffEvaluationDto = new StaffEvaluationDto();
//
//        staffEvaluationDto.setStars(stars);
//        staffEvaluationDto.setOpinion(opinion);
//        staffEvaluationDto.setPatient_Id(patient_Id);
//        staffEvaluationDto.setDoctor_Id(doctor_Id);
//
//        List<StaffEvaluationDto> patientsEvaluations = clinicService.getEvaluations_ByPatientId(currPatient.getPatient_id());
//
//        boolean doctorEvaluated = doctorAlreadyEvaluated(doctor_Id, patientsEvaluations);
//
//        if (stars != null && patient_Id > 0 && doctor_Id > 0 && !doctorEvaluated) {
//            clinicService.saveStaffEvaluation(staffEvaluationDto);
//        } else if (doctorEvaluated) {
//            Notification.show("You have already given an opinion to this doctor!");
//        }
//
//        refresh();
    }

    private boolean doctorAlreadyEvaluated(Integer doctorId, List<StaffEvaluationDto> patientsEvaluations) {
        return patientsEvaluations.stream()
                .anyMatch(evaluationDto -> evaluationDto.getDoctor_Id().equals(doctorId));
    }

    private VerticalLayout myScheduledAppointmentsLayout() {
        VerticalLayout myScheduledAppointmentsLayout = new VerticalLayout();

        myAppointmentToCancelTextField.setReadOnly(true);

        hideMyAppointmentsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        hideMyAppointmentsButton.addClickListener(event -> myScheduledAppointmentsLayout.setVisible(false));

        cancelAppointmentButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        VerticalLayout filter_Hide = new VerticalLayout(filterMyAppointmentsByTime, hideMyAppointmentsButton);
        VerticalLayout thisAppointment_Cancel = new VerticalLayout(myAppointmentToCancelTextField, cancelAppointmentButton);

        HorizontalLayout layoutTop = new HorizontalLayout(filter_Hide, thisAppointment_Cancel);
        thisAppointment_Cancel.setHorizontalComponentAlignment(FlexComponent.Alignment.BASELINE);

        patientScheduledAppointmentsGrid.setColumns("status", "dateTime");
        patientScheduledAppointmentsGrid.addColumn(appointmentDto ->
                "Patient: " + clinicService.getPatientById(appointmentDto.getPatientId()).getName()
                        + " " + clinicService.getPatientById(appointmentDto.getPatientId()).getSurname());
        patientScheduledAppointmentsGrid.addColumn(appointmentDto ->
                "Doctor: " + clinicService.getDoctorById(appointmentDto.getDoctorId()).getName()
                        + " " + clinicService.getDoctorById(appointmentDto.getDoctorId()).getSurname() + " M.D. Specialization: " +
                        clinicService.getDoctorById(appointmentDto.getDoctorId()).getSpecialization());

        setColumnNames_ScheduledAppointments(patientScheduledAppointmentsGrid);

        patientScheduledAppointmentsGrid.asSingleSelect()
                .addValueChangeListener(
                        event -> {
                            AppointmentDto gridCurrVal = patientScheduledAppointmentsGrid.asSingleSelect().getValue();
                            myAppointmentToCancelTextField.setValue(
                                    "Dr " + clinicService.getDoctorById(gridCurrVal.getDoctorId()).getSurname() + " | Specialization: " +
                                            clinicService.getDoctorById(gridCurrVal.getDoctorId()).getSpecialization() +
                                    " | Visit time: " + gridCurrVal.getDateTime());
                        });
        myAppointmentToCancelTextField.setMinWidth("40em");

        patientScheduledAppointmentsGrid.setMinWidth("90em");

        myScheduledAppointmentsLayout.add(layoutTop, patientScheduledAppointmentsGrid);
        return myScheduledAppointmentsLayout;
    }

    private void setColumnNames_ScheduledAppointments(Grid<AppointmentDto> grid) {
        String propertyId1 = "status";
        Grid.Column<AppointmentDto> col1 = grid.getColumnByKey(propertyId1);

        if (col1 != null) {
            col1.setHeader("Visit status");
        }
    }

    public void refresh() {
//        patientAppointmentsGrid.getDataProvider().refreshAll();
//        patientAppointmentsGrid.setItems(clinicService.getEvaluations_ByPatientId(currPatient.getPatient_id()));
    }

    private void updateMyScheduledAppointments() {
//        if(filterMyAppointmentsTime.isEmpty()) {
//            refresh();
//        } else {
//            workingDayGrid.setItems(clinicService.getWorkingDays_ByWeekday(filterMyAppointmentsTime.getValue()));
//        }
    }

    private void updateDoctorsAppointments() {
//        if(filterMyAppointmentsTime.isEmpty()) {
//            refresh();
//        } else {
//            workingDayGrid.setItems(clinicService.getWorkingDays_ByWeekday(filterMyAppointmentsTime.getValue()));
//        }
    }
}
