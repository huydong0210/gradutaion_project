package com.huydong.daotao.security.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huydong.daotao.service.dto.UserInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
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

    @Value("${keycloak.response_type}")
    private String responseType;

    @Value("${keycloak.client_id}")
    private String clientId;

    @Value("${keycloak.client_secret}")
    private String clientSecret;

    @Value("${keycloak.redirect_uri}")
    private String redirectUri;

    @Value("${keycloak.authorization_endpoint}")
    private String authorizationEndpoint;

    @Value("${keycloak.authorization_host}")
    private String authorizationHost;

    @Value("${keycloak.scope}")
    private List<String> scope;

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
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays
            .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .filter(auth -> !auth.trim().isEmpty())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public Authentication getAuthentication(UserInfo userInfo, String token) {

        Collection<? extends GrantedAuthority> authorities;

        authorities= userInfo.getRoles().stream().map(m -> new SimpleGrantedAuthority(m)).collect(Collectors.toList());
        //        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        //        Collection<? extends GrantedAuthority> authorities = Arrays
        //            .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
        //            .filter(auth -> !auth.trim().isEmpty())
        //            .map(SimpleGrantedAuthority::new)
        //            .collect(Collectors.toList());

        User principal = new User(userInfo.getUsername(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }

    public Optional<UserInfo> getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        setTimeOut(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id",clientId);
        formData.add("client_secret", clientSecret);
        formData.add("token", accessToken);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(formData, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8090/realms/UET_Authorization_Server/protocol/openid-connect/token/introspect",
                HttpMethod.POST,
                entity,
                String.class
            );
            if (response.hasBody()) {
                ObjectMapper objectMapper = new ObjectMapper();
                TypeReference<List<String>> roles = new TypeReference<List<String>>() {};
                JSONObject jsonObject = new JSONObject(response.getBody());
                UserInfo result = new UserInfo();
                result.setEmail(jsonObject.get("email").toString());
                result.setName(jsonObject.get("name").toString());
                result.setUsername(jsonObject.get("preferred_username").toString());
                String scope = jsonObject.getString("scope");
                result.setRoles(Arrays.asList(scope.split(" ")));
                return Optional.ofNullable(result);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return Optional.empty();
    }

    private static void setTimeOut(RestTemplate restTemplate) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        int time = 120000;
        requestFactory.setReadTimeout(time);
        requestFactory.setConnectTimeout(time);
        restTemplate.setRequestFactory(requestFactory);
    }
}
