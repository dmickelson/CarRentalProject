package com.company.carrental.ui.views;

import com.company.carrental.dto.CarTypeDTO;
import com.company.carrental.service.CarTypeService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "cartypes", layout = MainLayout.class)
public class CarTypeManagementView extends VerticalLayout {
    private final CarTypeService carTypeService;
    private Grid<CarTypeDTO> carTypeGrid = new Grid<>(CarTypeDTO.class);

    public CarTypeManagementView(CarTypeService carTypeService) {
        this.carTypeService = carTypeService;
        setSizeFull();
        configureGrid();
        add(carTypeGrid);
        refreshGrid();
    }

    private void configureGrid() {
        carTypeGrid.setSizeFull();
        carTypeGrid.removeAllColumns();
        carTypeGrid.addColumn(CarTypeDTO::getCarTypeId).setHeader("ID");
        carTypeGrid.addColumn(CarTypeDTO::getVehicleType).setHeader("Vehicle Type");
    }

    private void refreshGrid() {
        carTypeGrid.setItems(carTypeService.getAllCarTypes());
    }
}
