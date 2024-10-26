package com.company.carrental.ui.views;

import com.company.carrental.dto.UserDTO;
import com.company.carrental.service.UserService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

@Route(value = "users", layout = MainLayout.class)
public class UserManagementView extends VerticalLayout {
    private final UserService userService;

    private Grid<UserDTO> userGrid = new Grid<>(UserDTO.class);
    private FormLayout userForm = new FormLayout();

    private Registration saveButtonClickRegistration;

    public UserManagementView(UserService userService) {
        this.userService = userService;
        setSizeFull();

        configureGrid();
        configureForm();

        Button registerButton = new Button("Register User");
        registerButton.addClickListener(e -> showRegistrationForm());

        add(registerButton, userGrid, userForm);

        // Load initial data
        refreshGrid();
    }

    private void configureGrid() {
        userGrid.setSizeFull();
        userGrid.removeAllColumns(); // Clear default columns

        // Add specific columns in desired order
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

        // Get form fields
        TextField username = (TextField) userForm.getChildren()
                .filter(component -> component instanceof TextField)
                .filter(component -> ((TextField) component).getLabel().equals("Username"))
                .findFirst()
                .orElse(null);
        TextField firstName = (TextField) userForm.getChildren()
                .filter(component -> component instanceof TextField)
                .filter(component -> ((TextField) component).getLabel().equals("First Name"))
                .findFirst()
                .orElse(null);
        TextField lastName = (TextField) userForm.getChildren()
                .filter(component -> component instanceof TextField)
                .filter(component -> ((TextField) component).getLabel().equals("Last Name"))
                .findFirst()
                .orElse(null);

        // Get save button
        Button saveButton = (Button) userForm.getChildren()
                .filter(component -> component instanceof Button)
                .findFirst()
                .orElse(null);

        if (saveButtonClickRegistration != null) {
            saveButtonClickRegistration.remove();
        }
        saveButtonClickRegistration = saveButton.addClickListener(event -> {
            // Validate all fields are filled
            if (username.getValue().isEmpty() || firstName.getValue().isEmpty() || lastName.getValue().isEmpty()) {
                Notification.show("All fields must be filled", 3000, Notification.Position.MIDDLE);
                return;
            }

            UserDTO newUser = new UserDTO();
            newUser.setUsername(username.getValue());
            newUser.setFirstName(firstName.getValue());
            newUser.setLastName(lastName.getValue());

            userService.registerUser(newUser);
            userForm.setVisible(false);
            refreshGrid();

            // Clear form fields
            username.clear();
            firstName.clear();
            lastName.clear();

            Notification.show("User registered successfully");
        });

    }

    private void refreshGrid() {
        userGrid.setItems(userService.getAllUsers());
    }

    private void editUser(UserDTO user) {
        userForm.setVisible(true);

        // Get form fields
        TextField username = (TextField) userForm.getChildren()
                .filter(component -> component instanceof TextField)
                .filter(component -> ((TextField) component).getLabel().equals("Username"))
                .findFirst()
                .orElse(null);
        TextField firstName = (TextField) userForm.getChildren()
                .filter(component -> component instanceof TextField)
                .filter(component -> ((TextField) component).getLabel().equals("First Name"))
                .findFirst()
                .orElse(null);
        TextField lastName = (TextField) userForm.getChildren()
                .filter(component -> component instanceof TextField)
                .filter(component -> ((TextField) component).getLabel().equals("Last Name"))
                .findFirst()
                .orElse(null);

        // Populate form with user data
        username.setValue(user.getUsername());
        firstName.setValue(user.getFirstName());
        lastName.setValue(user.getLastName());

        // Get save button
        Button saveButton = (Button) userForm.getChildren()
                .filter(component -> component instanceof Button)
                .findFirst()
                .orElse(null);

        if (saveButtonClickRegistration != null) {
            saveButtonClickRegistration.remove();
        }

        saveButtonClickRegistration = saveButton.addClickListener(event -> {
            // Validate all fields are filled
            if (username.getValue().isEmpty() || firstName.getValue().isEmpty() || lastName.getValue().isEmpty()) {
                Notification.show("All fields must be filled", 3000, Notification.Position.MIDDLE);
                return;
            }

            // Update user data
            UserDTO updatedUser = new UserDTO();
            updatedUser.setUserId(user.getUserId());
            updatedUser.setUsername(username.getValue());
            updatedUser.setFirstName(firstName.getValue());
            updatedUser.setLastName(lastName.getValue());

            userService.updateUser(updatedUser.getUserId(), updatedUser);
            userForm.setVisible(false);
            refreshGrid();

            // Clear form fields
            username.clear();
            firstName.clear();
            lastName.clear();

            Notification.show("User updated successfully");
        });

    }

    private void deleteUser(UserDTO user) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Confirm Delete");
        dialog.setText("Are you sure you want to delete user: " + user.getUsername() + "?");

        dialog.setCancelable(true);
        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");

        dialog.addConfirmListener(event -> {
            userService.deleteUser(user.getUserId());
            refreshGrid();
            Notification.show("User deleted successfully");
        });

        dialog.open();
    }

}
