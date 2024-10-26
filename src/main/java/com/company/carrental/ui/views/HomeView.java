package com.company.carrental.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.icon.Icon;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Home | My Rental Car Company")
public class HomeView extends VerticalLayout {
    public HomeView() {
        addClassName("home-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(createHeader());
        add(createFeaturesSection());
        add(createServicesSection());
    }

    private VerticalLayout createHeader() {
        VerticalLayout header = new VerticalLayout();
        header.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H1 title = new H1("Welcome to My Rental Car Company");
        Paragraph subtitle = new Paragraph("Your Premier Car Rental Service");

        header.add(title, subtitle);
        return header;
    }

    private HorizontalLayout createFeaturesSection() {
        HorizontalLayout features = new HorizontalLayout();
        features.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        features.add(createFeatureCard(VaadinIcon.CAR, "Wide Selection", "Choose from our diverse fleet of vehicles"));
        features.add(createFeatureCard(VaadinIcon.MONEY, "Best Rates", "Competitive pricing for all budgets"));
        features.add(createFeatureCard(VaadinIcon.CLOCK, "24/7 Service", "Round-the-clock customer support"));

        return features;
    }

    private VerticalLayout createFeatureCard(VaadinIcon icon, String title, String description) {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("feature-card");
        card.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        Icon featureIcon = icon.create();
        featureIcon.setSize("50px");
        H2 featureTitle = new H2(title);
        Paragraph featureDesc = new Paragraph(description);

        card.add(featureIcon, featureTitle, featureDesc);
        return card;
    }

    private VerticalLayout createServicesSection() {
        VerticalLayout services = new VerticalLayout();
        services.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H2 servicesTitle = new H2("Our Services");
        Paragraph servicesDesc = new Paragraph("Experience hassle-free car rentals with our easy booking system");

        services.add(servicesTitle, servicesDesc);
        return services;
    }
}
