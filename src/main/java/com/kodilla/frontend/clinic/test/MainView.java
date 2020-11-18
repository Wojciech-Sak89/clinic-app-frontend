package com.kodilla.frontend.clinic.test;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("app")
public class MainView extends VerticalLayout {

    public MainView() {
        add(new Button("Click me", e -> Notification.show("Hello World")));
    }
}
