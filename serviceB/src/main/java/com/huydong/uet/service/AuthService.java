package com.huydong.uet.service;

import com.huydong.uet.service.dto.JWTToken;
import com.huydong.uet.service.dto.UserInfo;
import java.util.Optional;

public interface AuthService {
    Optional<JWTToken> getAccessTokenFromCode(String code);
    Optional<UserInfo> getUserInfo(String accessToken);
    String getLogOutUri(String idToken);
}
