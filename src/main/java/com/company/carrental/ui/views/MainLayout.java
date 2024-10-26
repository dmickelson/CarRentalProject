package com.company.carrental.ui.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.Component;

public class MainLayout extends AppLayout {
    public MainLayout() {
        createHeader();
        createNavigation();
    }

    private void createHeader() {
        H1 logo = new H1("My Rental Car Company");
        logo.addClassNames("text-l", "m-m");
        addToNavbar(new DrawerToggle(), logo);
    }

    private void createNavigation() {
        Tabs tabs = new Tabs();
        tabs.add(
                createTab(VaadinIcon.HOME, "Home", HomeView.class),
                createTab(VaadinIcon.CAR, "Cars", CarManagementView.class),
                createTab(VaadinIcon.CALENDAR, "Reservations", ReservationManagementView.class),
                createTab(VaadinIcon.USERS, "Users", UserManagementView.class));
        addToNavbar(tabs);
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, Class<? extends Component> viewClass) {
        RouterLink link = new RouterLink();
        link.add(viewIcon.create());
        link.add(viewName);
        link.setRoute(viewClass);
        return new Tab(link);
    }
}