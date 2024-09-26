package iocode.web.app.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * This class represents a User's data transfer object (DTO). It is used to transfer user information between layers of an application.
 *
 * @author YourName
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    /**
     * The user's first name.
     */
    private String firstname;

    /**
     * The user's last name.
     */
    private String lastname;

    /**
     * The user's unique username.
     */
    private String username;

    /**
     * The user's date of birth.
     */
    private Date dob;

    /**
     * The user's telephone number.
     */
    private long tel;

    /**
     * The user's password.
     * <p>
     * Note: It is recommended to store hashed passwords for security reasons.
     * </p>
     */
    private String password;

    /**
     * The user's gender.
     */
    private String gender;
}
