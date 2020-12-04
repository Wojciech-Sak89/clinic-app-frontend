package com.kodilla.clinic.ui.views.patient;

import ch.qos.logback.core.Layout;
import com.kodilla.clinic.backend.enums.Stars;
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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "patientOpinion", layout = MainLayout.class)
@PageTitle("Rate doctor | Clinic App")
public class PatientEvaluatesFormView extends FormLayout {

    private BigDecimalField peselField = new BigDecimalField("Please enter your PESEL number");

    private TextField greetingTextField = new TextField("YOU ARE SIGNED IN!");

    private ComboBox<DoctorDto> doctorsComboBox = new ComboBox<>("Choose doctor");
    private ComboBox<Stars> rateCombobox = new ComboBox<>("Rate ONE to FIVE stars \u2605");
    private TextArea opinionTextArea = new TextArea("Write your opinion");


    private Button signInButton = new Button("Sign in");
    private Button addOpinionButton = new Button("Add opinion");

    private Button seeYourOpinionsButton = new Button("Browse your opinions");

    private ClinicService clinicService;

    private VerticalLayout evaluationLayout;

    private PatientDto currPatient;

    private Grid<StaffEvaluationDto> staffEvaluationGrid = new Grid<>(StaffEvaluationDto.class);

    public PatientEvaluatesFormView(ClinicService clinicService) {
        this.clinicService = clinicService;

        peselField.setMinWidth("13em");

        greetingTextField.setReadOnly(true);

        doctorsComboBox.setItems(clinicService.getDoctors());
        doctorsComboBox.setItemLabelGenerator(doctorDto ->
                doctorDto.getName() + " " + doctorDto.getSurname() + " M.D. " +
                        "Specialization: " + doctorDto.getSpecialization() +
                        " Department: " + doctorDto.getDepartment());

        rateCombobox.setItems(Stars.values());

        VerticalLayout peselSignInLayout = new VerticalLayout(peselField, signInButton);

        evaluationLayout = new VerticalLayout(
                greetingTextField,
                doctorsComboBox,
                rateCombobox,
                opinionTextArea,
                addOpinionButton,
                seeYourOpinionsButton);
        evaluationLayout.setVisible(false);

        VerticalLayout fullFormLayout = new VerticalLayout(peselSignInLayout, evaluationLayout);

        Component opinionsLayout = opinionsLayout();
        opinionsLayout.setVisible(false);

        signInButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        signInButton.addClickListener(event -> signIn());

        addOpinionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addOpinionButton.addClickListener(event -> addOpinion());

        seeYourOpinionsButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        seeYourOpinionsButton.addClickListener(event -> opinionsLayout.setVisible(true));

        HorizontalLayout formGridLayout = new HorizontalLayout(fullFormLayout, opinionsLayout);

        add(formGridLayout);
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

            evaluationLayout.setVisible(true);
            greetingTextField.setValue("Hi " + currPatient.getName() + " " + currPatient.getSurname() + "!");

            staffEvaluationGrid.setItems(clinicService.getEvaluations_ByPatientId(currPatient.getPatient_id()));
        }
    }

    private void addOpinion() {
        Stars stars = null;
        String opinion = "";
        Integer doctor_Id = 0;
        Integer patient_Id = clinicService.getPatient_ByPesel(
                peselField.getValue())
                .getPatient_id();

        if (!rateCombobox.isEmpty()) stars = rateCombobox.getValue();
        if (!opinionTextArea.isEmpty()) opinion = opinionTextArea.getValue();
        if (!doctorsComboBox.isEmpty()) doctor_Id = doctorsComboBox.getValue().getDoctor_id();

        StaffEvaluationDto staffEvaluationDto = new StaffEvaluationDto();

        staffEvaluationDto.setStars(stars);
        staffEvaluationDto.setOpinion(opinion);
        staffEvaluationDto.setPatient_Id(patient_Id);
        staffEvaluationDto.setDoctor_Id(doctor_Id);

        List<StaffEvaluationDto> patientsEvaluations = clinicService.getEvaluations_ByPatientId(currPatient.getPatient_id());

        boolean doctorEvaluated = doctorAlreadyEvaluated(doctor_Id, patientsEvaluations);

        if (stars != null && patient_Id > 0 && doctor_Id > 0 && !doctorEvaluated) {
            clinicService.saveStaffEvaluation(staffEvaluationDto);
        } else if (doctorEvaluated) {
            Notification.show("You have already given an opinion to this doctor!");
        }

        refresh();
    }

    private boolean doctorAlreadyEvaluated(Integer doctorId, List<StaffEvaluationDto> patientsEvaluations) {
        return patientsEvaluations.stream()
                .anyMatch(evaluationDto -> evaluationDto.getDoctor_Id().equals(doctorId));
    }

    private Component opinionsLayout() {
        VerticalLayout layout = new VerticalLayout();

        staffEvaluationGrid.setColumns("stars", "opinion", "entryDate");
        staffEvaluationGrid.addColumn(evaluation ->
                "Patient: " +clinicService.getPatientById(evaluation.getPatient_Id()).getName()
                        + " " + clinicService.getPatientById(evaluation.getPatient_Id()).getSurname());
        staffEvaluationGrid.addColumn(evaluation ->
                "Doctor: " + clinicService.getDoctorById(evaluation.getDoctor_Id()).getName()
                        + " " + clinicService.getDoctorById(evaluation.getDoctor_Id()).getSurname());

        setColumnNames(staffEvaluationGrid);

        staffEvaluationGrid.setWidth("80em");
        layout.add(staffEvaluationGrid);

        return layout;
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
        staffEvaluationGrid.setItems(clinicService.getEvaluations_ByPatientId(currPatient.getPatient_id()));
    }
}
