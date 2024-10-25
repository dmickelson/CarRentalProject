package com.company.carrental.ui.views;

import com.company.carrental.dto.CarDTO;
import com.company.carrental.entity.Car.CarStatus;
import com.company.carrental.entity.CarType.VechicleType;
import com.company.carrental.service.CarService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@Route("cars")
public class CarManagementView extends VerticalLayout {
    private final CarService carService;
    private Grid<CarDTO> carGrid = new Grid<>(CarDTO.class);
    private FormLayout carForm = new FormLayout();
    private Registration saveButtonClickRegistration;

    public CarManagementView(CarService carService) {
        this.carService = carService;
        setSizeFull();

        configureGrid();
        configureForm();

        Button addCarButton = new Button("Add New Car");
        addCarButton.addClickListener(e -> showCarForm());

        add(addCarButton, carGrid, carForm);
        refreshGrid();
    }

    private void configureGrid() {
        carGrid.setSizeFull();
        carGrid.removeAllColumns();

        carGrid.addColumn(CarDTO::getCarId).setHeader("ID");
        carGrid.addColumn(CarDTO::getVehicleType).setHeader("Vehicle Type");
        carGrid.addColumn(CarDTO::getStatus).setHeader("Status");

        carGrid.addComponentColumn(car -> {
            Button editButton = new Button("Edit");
            Button deleteButton = new Button("Delete");

            editButton.addClickListener(e -> editCar(car));
            deleteButton.addClickListener(e -> deleteCar(car));

            return new HorizontalLayout(editButton, deleteButton);
        });
    }

    private void configureForm() {
        ComboBox<VechicleType> vehicleType = new ComboBox<>("Vehicle Type");
        vehicleType.setItems(VechicleType.values());

        ComboBox<CarStatus> status = new ComboBox<>("Status");
        status.setItems(CarStatus.values());

        Button saveButton = new Button("Save");

        carForm.add(vehicleType, status, saveButton);
        carForm.setVisible(false);
    }

    private void showCarForm() {
        carForm.setVisible(true);
        clearForm();
        configureSaveButton(null);
    }

    private void editCar(CarDTO car) {
        carForm.setVisible(true);

        ComboBox<VechicleType> vehicleType = (ComboBox<VechicleType>) carForm.getChildren()
                .filter(component -> component instanceof ComboBox<?>)
                .filter(component -> ((ComboBox<?>) component).getLabel().equals("Vehicle Type"))
                .findFirst()
                .orElse(null);

        ComboBox<CarStatus> status = (ComboBox<CarStatus>) carForm.getChildren()
                .filter(component -> component instanceof ComboBox<?>)
                .filter(component -> ((ComboBox<?>) component).getLabel().equals("Status"))
                .findFirst()
                .orElse(null);

        vehicleType.setValue(car.getVehicleType());
        status.setValue(car.getStatus());

        configureSaveButton(car);
    }

    private void configureSaveButton(CarDTO existingCar) {
        Button saveButton = (Button) carForm.getChildren()
                .filter(component -> component instanceof Button)
                .findFirst()
                .orElse(null);

        if (saveButtonClickRegistration != null) {
            saveButtonClickRegistration.remove();
        }

        saveButtonClickRegistration = saveButton.addClickListener(event -> {
            ComboBox<VechicleType> vehicleType = (ComboBox<VechicleType>) carForm.getChildren()
                    .filter(component -> component instanceof ComboBox<?>)
                    .filter(component -> ((ComboBox<?>) component).getLabel().equals("Vehicle Type"))
                    .findFirst()
                    .orElse(null);

            ComboBox<CarStatus> status = (ComboBox<CarStatus>) carForm.getChildren()
                    .filter(component -> component instanceof ComboBox<?>)
                    .filter(component -> ((ComboBox<?>) component).getLabel().equals("Status"))
                    .findFirst()
                    .orElse(null);

            if (vehicleType.getValue() == null || status.getValue() == null) {
                Notification.show("All fields must be filled", 3000, Notification.Position.MIDDLE);
                return;
            }

            CarDTO carDTO = new CarDTO();
            if (existingCar != null) {
                carDTO.setCarId(existingCar.getCarId());
            }
            carDTO.setVehicleType(vehicleType.getValue());
            carDTO.setStatus(status.getValue());

            if (existingCar != null) {
                carService.updateCar(existingCar.getCarId(), carDTO);
                Notification.show("Car updated successfully");
            } else {
                carService.createCar(carDTO);
                Notification.show("Car added successfully");
            }

            carForm.setVisible(false);
            clearForm();
            refreshGrid();
        });
    }

    private void deleteCar(CarDTO car) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Confirm Delete");
        dialog.setText("Are you sure you want to delete car ID: " + car.getCarId() + "?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");

        dialog.addConfirmListener(event -> {
            carService.deleteCar(car.getCarId());
            refreshGrid();
            Notification.show("Car deleted successfully");
        });

        dialog.open();
    }

    private void clearForm() {
        ComboBox<VechicleType> vehicleType = (ComboBox<VechicleType>) carForm.getChildren()
                .filter(component -> component instanceof ComboBox<?>)
                .filter(component -> ((ComboBox<?>) component).getLabel().equals("Vehicle Type"))
                .findFirst()
                .orElse(null);

        ComboBox<CarStatus> status = (ComboBox<CarStatus>) carForm.getChildren()
                .filter(component -> component instanceof ComboBox<?>)
                .filter(component -> ((ComboBox<?>) component).getLabel().equals("Status"))
                .findFirst()
                .orElse(null);

        if (vehicleType != null)
            vehicleType.clear();
        if (status != null)
            status.clear();
    }

    private void refreshGrid() {
        carGrid.setItems(carService.getAllCars());
    }
}
