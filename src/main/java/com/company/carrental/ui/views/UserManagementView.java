package com.company.carrental.ui.views;

import com.company.carrental.dto.UserDTO;
import com.company.carrental.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

@Route("users")
public class UserManagementView extends VerticalLayout {
    private final UserService userService;

    private Grid<UserDTO> userGrid = new Grid<>(UserDTO.class);
    private FormLayout userForm = new FormLayout();

    public UserManagementView(UserService userService) {
        this.userService = userService;
        setSizeFull();

        configureGrid();
        configureForm();

        Button registerButton = new Button("Register User");
        registerButton.addClickListener(e -> showRegistrationForm());

        add(registerButton, userGrid, userForm);
    }

    private void configureGrid() {
        userGrid.setSizeFull();
        userGrid.addColumn(UserDTO::getUserId).setHeader("ID");
        userGrid.addColumn(UserDTO::getUsername).setHeader("Username");
        userGrid.addColumn(UserDTO::getFirstName).setHeader("First Name");
        userGrid.addColumn(UserDTO::getLastName).setHeader("Last Name");

        // Add edit/delete buttons column
        userGrid.addComponentColumn(user -> {
            Button editButton = new Button("Edit");
            Button deleteButton = new Button("Delete");

            editButton.addClickListener(e -> editUser(user));
            deleteButton.addClickListener(e -> deleteUser(user));

            return new HorizontalLayout(editButton, deleteButton);
        });
    }

    private void configureForm() {
        TextField username = new TextField("Username");
        TextField firstName = new TextField("First Name");
        TextField lastName = new TextField("Last Name");

        userForm.add(username, firstName, lastName);

        Button saveButton = new Button("Save");
        userForm.add(saveButton);

        userForm.setVisible(false);
    }

    private void showRegistrationForm() {
        userForm.setVisible(true);
        // Add form submission handler that calls userService.registerUser()
    }

    private void editUser(UserDTO user) {
        // Populate form with user data
        // Add form submission handler that calls userService.updateUser()
    }

    private void deleteUser(UserDTO user) {
        userService.deleteUser(user.getUserId());
        // Refresh grid data
    }

}
