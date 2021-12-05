//package com.eleonoralion.servingwebcontent.entity;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "usr_info")
//public class UserInfo {
//    @Id
//    private Long id;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @MapsId
//    @JoinColumn(name = "id")
//    private User user;
//
//    private String firstName;
//    private String lastName;
//    private Integer age;
//
//    public UserInfo() {
//        System.out.println("new UserInfo");
//        firstName = "";
//        lastName = "";
//        age = 0;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public Integer getAge() {
//        return age;
//    }
//
//    public void setAge(Integer age) {
//        this.age = age;
//    }
//}
