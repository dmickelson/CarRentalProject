package com.company.carrental.ui.views;

import com.company.carrental.dto.ReservationDTO;
import com.company.carrental.dto.CarDTO;
import com.company.carrental.dto.UserDTO;
import com.company.carrental.entity.Reservation.ReservationStatus;
import com.company.carrental.service.ReservationService;
import com.company.carrental.service.CarService;
import com.company.carrental.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@Route(value = "reservations", layout = MainLayout.class)
public class ReservationManagementView extends VerticalLayout {
    private final ReservationService reservationService;
    private final CarService carService;
    private final UserService userService;
    private Grid<ReservationDTO> reservationGrid = new Grid<>(ReservationDTO.class);
    private FormLayout reservationForm = new FormLayout();
    private Registration saveButtonClickRegistration;

    public ReservationManagementView(ReservationService reservationService, CarService carService,
            UserService userService) {
        this.reservationService = reservationService;
        this.carService = carService;
        this.userService = userService;
        setSizeFull();

        configureGrid();
        configureForm();

        Button addReservationButton = new Button("Add New Reservation");
        addReservationButton.addClickListener(e -> showReservationForm());

        add(addReservationButton, reservationGrid, reservationForm);
        refreshGrid();
    }

    private void configureGrid() {
        reservationGrid.setSizeFull();
        reservationGrid.removeAllColumns();

        reservationGrid.addColumn(res -> res.getUser().getUsername()).setHeader("User");
        reservationGrid.addColumn(res -> res.getCar().getVehicleType()).setHeader("Car Type");
        reservationGrid.addColumn(ReservationDTO::getStartDate).setHeader("Start Date");
        reservationGrid.addColumn(ReservationDTO::getEndDate).setHeader("End Date");
        reservationGrid.addColumn(ReservationDTO::getStatus).setHeader("Status");

        reservationGrid.addComponentColumn(reservation -> {
            Button editButton = new Button("Edit");
            Button cancelButton = new Button("Cancel");

            editButton.addClickListener(e -> editReservation(reservation));
            cancelButton.addClickListener(e -> cancelReservation(reservation));

            return new HorizontalLayout(editButton, cancelButton);
        });
    }

    private void configureForm() {
        ComboBox<UserDTO> userSelect = new ComboBox<>("User");
        userSelect.setItems(userService.getAllUsers());
        userSelect.setItemLabelGenerator(UserDTO::getUsername);

        ComboBox<CarDTO> carSelect = new ComboBox<>("Car");
        carSelect.setItems(carService.getAvailableCars());
        carSelect.setItemLabelGenerator(car -> car.getVehicleType().toString());

        DatePicker startDate = new DatePicker("Start Date");
        DatePicker endDate = new DatePicker("End Date");

        Button saveButton = new Button("Save");

        reservationForm.add(userSelect, carSelect, startDate, endDate, saveButton);
        reservationForm.setVisible(false);
    }

    private void showReservationForm() {
        reservationForm.setVisible(true);
        clearForm();
        configureSaveButton(null);
    }

    private void editReservation(ReservationDTO reservation) {
        reservationForm.setVisible(true);

        ComboBox<UserDTO> userSelect = (ComboBox<UserDTO>) reservationForm.getChildren()
                .filter(component -> component instanceof ComboBox<?>)
                .filter(component -> ((ComboBox<?>) component).getLabel().equals("User"))
                .findFirst()
                .orElse(null);

        ComboBox<CarDTO> carSelect = (ComboBox<CarDTO>) reservationForm.getChildren()
                .filter(component -> component instanceof ComboBox<?>)
                .filter(component -> ((ComboBox<?>) component).getLabel().equals("Car"))
                .findFirst()
                .orElse(null);

        DatePicker startDate = (DatePicker) reservationForm.getChildren()
                .filter(component -> component instanceof DatePicker)
                .filter(component -> ((DatePicker) component).getLabel().equals("Start Date"))
                .findFirst()
                .orElse(null);

        DatePicker endDate = (DatePicker) reservationForm.getChildren()
                .filter(component -> component instanceof DatePicker)
                .filter(component -> ((DatePicker) component).getLabel().equals("End Date"))
                .findFirst()
                .orElse(null);

        userSelect.setValue(reservation.getUser());
        carSelect.setValue(reservation.getCar());
        startDate.setValue(reservation.getStartDate());
        endDate.setValue(reservation.getEndDate());

        configureSaveButton(reservation);
    }

    private void configureSaveButton(ReservationDTO existingReservation) {
        Button saveButton = (Button) reservationForm.getChildren()
                .filter(component -> component instanceof Button)
                .findFirst()
                .orElse(null);

        if (saveButtonClickRegistration != null) {
            saveButtonClickRegistration.remove();
        }

        saveButtonClickRegistration = saveButton.addClickListener(event -> {
            try {
                ComboBox<UserDTO> userSelect = (ComboBox<UserDTO>) reservationForm.getChildren()
                        .filter(component -> component instanceof ComboBox<?>)
                        .filter(component -> ((ComboBox<?>) component).getLabel().equals("User"))
                        .findFirst()
                        .orElse(null);

                ComboBox<CarDTO> carSelect = (ComboBox<CarDTO>) reservationForm.getChildren()
                        .filter(component -> component instanceof ComboBox<?>)
                        .filter(component -> ((ComboBox<?>) component).getLabel().equals("Car"))
                        .findFirst()
                        .orElse(null);

                DatePicker startDate = (DatePicker) reservationForm.getChildren()
                        .filter(component -> component instanceof DatePicker)
                        .filter(component -> ((DatePicker) component).getLabel().equals("Start Date"))
                        .findFirst()
                        .orElse(null);

                DatePicker endDate = (DatePicker) reservationForm.getChildren()
                        .filter(component -> component instanceof DatePicker)
                        .filter(component -> ((DatePicker) component).getLabel().equals("End Date"))
                        .findFirst()
                        .orElse(null);

                if (userSelect.getValue() == null || carSelect.getValue() == null ||
                        startDate.getValue() == null || endDate.getValue() == null) {
                    Notification.show("All fields must be filled", 3000, Notification.Position.MIDDLE);
                    return;
                }

                ReservationDTO reservationDTO = new ReservationDTO();
                if (existingReservation != null) {
                    reservationDTO.setReservationId(existingReservation.getReservationId());
                }
                reservationDTO.setUser(userSelect.getValue());
                reservationDTO.setCar(carSelect.getValue());
                reservationDTO.setStartDate(startDate.getValue());
                reservationDTO.setEndDate(endDate.getValue());
                reservationDTO.setStatus(ReservationStatus.ACTIVE);

                if (existingReservation != null) {
                    reservationService.updateReservation(existingReservation.getReservationId(), reservationDTO);
                    Notification.show("Reservation updated successfully");
                } else {
                    reservationService.createReservation(reservationDTO);
                    Notification.show("Reservation created successfully");
                }

                reservationForm.setVisible(false);
                clearForm();
                refreshGrid();

            } catch (RuntimeException e) {
                Notification notification = Notification.show(
                        "Cannot create reservation: Car is already booked for these dates",
                        5000,
                        Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
    }

    private void cancelReservation(ReservationDTO reservation) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Confirm Cancellation");
        dialog.setText("Are you sure you want to cancel this reservation?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Cancel Reservation");
        dialog.setConfirmButtonTheme("error primary");

        dialog.addConfirmListener(event -> {
            reservationService.cancelReservation(reservation.getReservationId());
            refreshGrid();
            Notification.show("Reservation cancelled successfully");
        });

        dialog.open();
    }

    private void clearForm() {
        reservationForm.getChildren()
                .filter(component -> component instanceof ComboBox<?>)
                .forEach(component -> ((ComboBox<?>) component).clear());

        reservationForm.getChildren()
                .filter(component -> component instanceof DatePicker)
                .forEach(component -> ((DatePicker) component).clear());
    }

    private void refreshGrid() {
        reservationGrid.setItems(reservationService.getAllReservations());
    }
}
