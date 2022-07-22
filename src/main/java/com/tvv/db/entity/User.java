package com.tvv.db.entity;

import java.time.LocalDate;
import java.util.Objects;

public class User extends Entity{
    private String number;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phone;
    private String status;
    private String photo;
    private Role role;
    private String locale;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(number, user.number) && Objects.equals(password, user.password)
                && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName)
                && Objects.equals(dateOfBirth, user.dateOfBirth) && Objects.equals(phone, user.phone)
                && Objects.equals(status, user.status) && Objects.equals(photo, user.photo)
                && Objects.equals(role, user.role) && Objects.equals(locale, user.locale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, password, firstName, lastName, dateOfBirth, phone, status, photo, role, locale);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "User{" +
                "number='" + number + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", phone='" + phone + '\'' +
                ", status='" + status + '\'' +
                ", photo='" + photo + '\'' +
                ", role=" + role +
                '}';
    }
}
