package com.huydong.uet.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.huydong.uet.security.KeycloakProperties;
import com.huydong.uet.security.jwt.JWTFilter;
import com.huydong.uet.security.jwt.TokenProvider;
import com.huydong.uet.service.AuthService;
import com.huydong.uet.service.dto.JWTToken;
import com.huydong.uet.web.rest.vm.LoginVM;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final KeycloakProperties keycloakProperties;
    private final AuthService authService;

    public UserJWTController(
        TokenProvider tokenProvider,
        AuthenticationManagerBuilder authenticationManagerBuilder,
        KeycloakProperties keycloakProperties,
        AuthService authService
    ) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.keycloakProperties = keycloakProperties;
        this.authService = authService;
    }

    @GetMapping("/login")
    public ResponseEntity<AuthorizationUri> login() {
        return ResponseEntity.ok(new AuthorizationUri(keycloakProperties.createAuthorizationUri()));
    }

    @GetMapping("/access-token")
    public ResponseEntity<JWTToken> getAccessToken(@RequestParam String code) {
        JWTToken token = authService.getAccessTokenFromCode(code).orElseThrow(() -> new RuntimeException("code invalid"));
        return ResponseEntity.ok(token);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, loginVM.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    static class AuthorizationUri {

        private String authorizationUri;

        AuthorizationUri(String authorizationUri) {
            this.authorizationUri = authorizationUri;
        }

        @JsonProperty("authorization_uri")
        String getAuthorizationUri() {
            return authorizationUri;
        }
    }
}
