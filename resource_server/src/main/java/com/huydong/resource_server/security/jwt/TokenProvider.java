package com.huydong.resource_server.security.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import liquibase.pro.packaged.J;
import netscape.javascript.JSObject;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import tech.jhipster.config.JHipsterProperties;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private final Key key;

    private final JwtParser jwtParser;

    private final long tokenValidityInMilliseconds;

    private final long tokenValidityInMillisecondsForRememberMe;

    public TokenProvider(JHipsterProperties jHipsterProperties) {
        byte[] keyBytes;
        String secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getBase64Secret();
        if (!ObjectUtils.isEmpty(secret)) {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(secret);
        } else {
            log.warn(
                "Warning: the JWT key used is not Base64-encoded. " +
                "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security."
            );
            secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.tokenValidityInMilliseconds = 1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe =
            1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSecondsForRememberMe();
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts
            .builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();
    }

    public Authentication getAuthentication(String token) {


        String[] split_string = token.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];


        org.apache.commons.codec.binary.Base64 base64Url = new Base64(true);
        String header = new String(base64Url.decode(base64EncodedHeader));

        String body = new String(base64Url.decode(base64EncodedBody));
        JSONObject jsObject;
        try {
            jsObject = new JSONObject(body);
            Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(jsObject.get("scope").toString().split(" "))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

            User principal = new User(jsObject.get("preferred_username").toString(), "", authorities);

            return new UsernamePasswordAuthenticationToken(principal, token, authorities);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

//    public boolean validateToken(String authToken) {
//        try {
//            jwtParser.parseClaimsJws(authToken);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            log.info("Invalid JWT token.");
//            log.trace("Invalid JWT token trace.", e);
//        }
//        return false;
//    }
    public boolean validateToken(String jwt){
        RestTemplate restTemplate = new RestTemplate();
        setTimeOut(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(null, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8090/realms/UET_Authorization_Server/protocol/openid-connect/userinfo",
                HttpMethod.GET,
                entity,
                String.class
            );
            if (response.hasBody()) {
                return true;

            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return false;
    }
    private static void setTimeOut(RestTemplate restTemplate) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        int time = 120000;
        requestFactory.setReadTimeout(time);
        requestFactory.setConnectTimeout(time);
        restTemplate.setRequestFactory(requestFactory);
    }
}
