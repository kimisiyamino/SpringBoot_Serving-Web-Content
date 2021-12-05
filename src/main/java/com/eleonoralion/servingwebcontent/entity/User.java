package com.eleonoralion.servingwebcontent.entity;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Table(name = "usr")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @NotNull
    private Boolean confirmEmail;

    private String activationCode;

    @NotNull
    private Boolean active;

    @NotNull(message =  "Ошибка в Registration Date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", fallbackPatterns = { "yyyy-MM-dd'T'HH:mm" })
    private LocalDateTime registrationDate;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    //@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
   // private UserInfo userInfo;

    public User() {
        roles = new HashSet<>(); // ??
        //userInfo = new UserInfo();
    }

    // Конструктор для удаленного пользователя
    public User(String username) {
        this.username = username;
        password="";
        email = "";
        activationCode = "";
        active = true;
        confirmEmail=false;
        registrationDate = LocalDateTime.MIN;
       // userInfo = new UserInfo();
        //roles = Collections.singleton(Role.USER);
    }

    // Конструктор для регистрации
    public User(RegistrationForm registrationForm) {
        this.username = registrationForm.getUsername();
        this.password = registrationForm.getPassword();
        this.email = registrationForm.getEmail();
        this.activationCode = UUID.randomUUID().toString();
        this.active = false;
        this.registrationDate = LocalDateTime.now().withNano(0);
        this.roles = Collections.singleton(Role.USER); // ??
        userInfo = new UserInfo();
    }


    public boolean isAdmin(){
        return roles.contains(Role.ADMIN);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public String getFormatRegistrationDate() {
        return registrationDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(Boolean confirmEmail) {
        this.confirmEmail = confirmEmail;
    }

   // public UserInfo getUserInfo() {

 //       return userInfo;
 //   }

  //  public void setUserInfo(UserInfo userInfo) {
  //      this.userInfo = userInfo;
//    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", registrationDate=" + registrationDate +
                ", roles=" + roles +
                '}';
    }
}
