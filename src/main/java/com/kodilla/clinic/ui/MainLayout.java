package com.kodilla.clinic.ui;

import com.kodilla.clinic.ui.admin.view.*;
import com.kodilla.clinic.ui.patient.BookAppointmentFormView;
import com.kodilla.clinic.ui.patient.PatientEvaluatesFormView;
import com.kodilla.clinic.ui.patient.RecommendationFormView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;


@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {
    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Clinic App");
        logo.addClassName("logo");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.addClassName("header");
        header.setWidth("100%");
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    private void createDrawer() {
        Label adminLabel = new Label("ADMIN");
        Label patientLabel = new Label("PATIENT");

        RouterLink workingDaysLink = new RouterLink("Working Days", WorkingDaysView.class);
        workingDaysLink.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink emergencyHoursLink = new RouterLink("Emergency Hours", EmergencyHoursView.class);
        emergencyHoursLink.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink doctorsLink = new RouterLink("Doctors", DoctorsView.class);
        doctorsLink.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink docSchedulesLink = new RouterLink("Doctors schedules", DoctorScheduleView.class);
        docSchedulesLink.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink patientsLink = new RouterLink("Patients", PatientsView.class);
        patientsLink.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink evaluationLink = new RouterLink("Staff evaluations", StaffEvaluationsView.class);
        evaluationLink.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink appointmentsLink = new RouterLink("Appointments", AppointmentsView.class);
        appointmentsLink.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink recommendationLink = new RouterLink("Specialist recommendation", RecommendationFormView.class);
        recommendationLink.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink opinionLink = new RouterLink("Opinion", PatientEvaluatesFormView.class);
        opinionLink.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink reserveDoctorAppointmentLink = new RouterLink("Book doctor appointment", BookAppointmentFormView.class);
        reserveDoctorAppointmentLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                adminLabel,
                workingDaysLink,
                emergencyHoursLink,
                doctorsLink,
                docSchedulesLink,
                patientsLink,
                evaluationLink,
                appointmentsLink,

                patientLabel,
                recommendationLink,
                opinionLink,
                reserveDoctorAppointmentLink
        ));
    }
}
