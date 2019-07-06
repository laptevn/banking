package com.laptevn.bank.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Bank entity that contains data used via communication
 */
public class Bank {
    private int id;
    private String firstName;
    private String lastName;
    @JsonProperty("IBAN") private String iban;

    public int getId() {
        return id;
    }

    public Bank setId(int id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Bank setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Bank setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getIban() {
        return iban;
    }

    public Bank setIban(String iban) {
        this.iban = iban;
        return this;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", iban='" + iban + '\'' +
                '}';
    }
}