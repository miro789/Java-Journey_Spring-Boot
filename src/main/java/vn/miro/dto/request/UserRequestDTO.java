package vn.miro.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

public class UserRequestDTO implements Serializable {
    @NotBlank(message = "First name must be not blank")
    private String firstName;

    @NotNull(message = "Last name must be not null")
    private String lastName;

    @Pattern(regexp ="^\\d{10}$", message = "Email invalid format")
    private String phone;

    @Email(message = "Email invalid format")
    private String email;


    public UserRequestDTO(String firstName, String lastName, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
