package com.huydong.daotao.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JWTToken {

    private String accessToken;
    private String refreshToken;
    private String IdToken;

    public JWTToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public JWTToken() {}

    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("refresh_token")
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @JsonProperty("id_token")
    public String getIdToken() {
        return IdToken;
    }

    public void setIdToken(String idToken) {
        IdToken = idToken;
    }
}
