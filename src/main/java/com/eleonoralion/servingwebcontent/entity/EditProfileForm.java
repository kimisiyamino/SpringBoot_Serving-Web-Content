package com.eleonoralion.servingwebcontent.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EditProfileForm {

    private Long id;

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

    private boolean confirmEmail;

    @Size(min = 0, max = 16)
    private String firstName;
    @Size(min = 0, max = 16)
    private String lastName;
    private Integer age;

    public EditProfileForm() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(boolean confirmEmail) {
        this.confirmEmail = confirmEmail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "EditProfileForm{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", confirmEmail=" + confirmEmail +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }
}
