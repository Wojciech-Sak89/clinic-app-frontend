# clinic-app-frontend
Frontend in Vaadin for private clinic. Diploma project prepared during Kodilla bootcamp course.

## Startup manual
0. You might need to install node.js for global use on your PC for Vaadin (frontend framework) to work properly. You can get it here: https://nodejs.org/en/download/

1. Run clinic-app-backend. Repository link: https://github.com/Wojciech-Sak89/clinic-app-backend
2. Run clinic-app-frontend from this repository.
3. Run http://localhost:8081/ on your browser.
4. Clinic App is ready to use!

## Clinic App description

## Kodilla requirements
1. REST endpoints
    - Create at least 20 different endpoints.
    - Use each of the known HTTP methods at least once: GET, POST, PUT and DELETE
2. Downloading data
    - Use at least two external data sources.
3. Scheduler
    - Design and implement at least one scheduler usage.
4. Save records to database
    - Implement at least 10 different data writing operations to the database.
5. Tests
    - Create tests covering at least 65% of the code.
6. Design Patterns
    - Use at least two different design patterns.
7. View layer
    - Use the Vaadin library to create a view layer of your application.
  
### Ad. 1
  The system consists of 37 endpoints ready to use. Frontend effectively uses at least 25 of them with all known HTTP methods used at least once: GET, POST, PUT and DELETE.

### Ad. 2
  System uses:
  1. Open weather API (https://openweathermap.org/api) - clinic patient at 2 PM a day before his visit recieves an email message with weather forecast for time and place of his      visit.
  2. API Medic (https://apimedic.com/)- at subpage "PATIENT - Specialist recommendation" (http://localhost:8081/specialistRecommendation) patient can get a recommendation to         which doctor he or she should sign up for based on his or hers symptoms.

### Ad. 3
  Scheduler is used to send emails with reminder messages every day before working day at 2PM for those patients who have visit next day.

### Ad. 4
 The system consists of 7 entities which gives the minimum of 14 data writing operations to the database (creation and edition).

### Ad. 5
  Tests cover 86% of classes, 73% of methods and 69% of lines.

### Ad. 6
  System uses 3 design patterns:
    1. Factory (backend: package com.kodilla.clinic.domain.schedule.factory.ScheduleFactory)
    2. Builder (backend: package com.kodilla.clinic.domain.schedule.ClinicDoctorSchedule)
    3. Singleton (frontend: com.kodilla.clinic.ui.patient.grid.ScheduledAppointmentsGridView)

### Ad. 7
  Applied at https://github.com/Wojciech-Sak89/clinic-app-frontend
