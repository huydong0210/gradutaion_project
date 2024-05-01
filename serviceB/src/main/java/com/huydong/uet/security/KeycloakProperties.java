package com.huydong.uet.security;

import java.util.List;

public class KeycloakProperties {

    private String responseType;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authorizationEndpoint;
    private String authorizationHost;
    private List<String> scope;

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public void setAuthorizationEndpoint(String authorizationEndpoint) {
        this.authorizationEndpoint = authorizationEndpoint;
    }

    public String getAuthorizationHost() {
        return authorizationHost;
    }

    public void setAuthorizationHost(String authorizationHost) {
        this.authorizationHost = authorizationHost;
    }

    public List<String> getScope() {
        return scope;
    }

    public void setScope(List<String> scope) {
        this.scope = scope;
    }

    public String createAuthorizationUri() {
        StringBuilder result = new StringBuilder();
        result
            .append(authorizationHost)
            .append(authorizationEndpoint)
            .append("?")
            .append("response_type=")
            .append(responseType)
            .append("&")
            .append("client_id=")
            .append(clientId)
            .append("&")
            .append("redirect_uri=")
            .append(redirectUri)
            .append("&")
            .append("scope=");
        for (String s : scope) {
            if (scope.indexOf(s) != scope.size() - 1) {
                result.append(s).append("%20");
            } else {
                result.append(s);
            }
        }

        return result.toString();
    }
}
