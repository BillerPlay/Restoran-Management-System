package com.ironhack.restoranmanagementsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserSummary {
    private final Long id;
    @JsonProperty("full_name")
    private final String fullName;
    private final String email;

    public UserSummary(Long id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

}
