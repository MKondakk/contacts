package com.example.contacts;

public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    private void setToken(String token) {
        this.token = token;
    }
}

