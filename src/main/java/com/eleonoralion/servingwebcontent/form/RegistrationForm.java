package com.eleonoralion.servingwebcontent.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegistrationForm {

    @NotBlank(message = "Поле username пустое!")
    @Size(min = 4, max = 16, message = "Длинна username должна быть от 4 до 16 символов")
    @Pattern(regexp = "[a-zA-Z]{1}[a-zA-Z0-9]+", message = "Ошибка в username")
    private String username;

    @NotBlank(message = "Поле password пустое!")
    @Size(min = 4, max = 16, message = "Длинна пароля должна быть от 4 до 16 символов")
    private String password;

    @NotBlank(message = "Поле email пустое!")
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Некорректный email")
    private String email;

    public RegistrationForm() {
    }

    public RegistrationForm(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
