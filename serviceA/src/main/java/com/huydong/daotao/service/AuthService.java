package com.huydong.daotao.service;

import com.huydong.daotao.service.dto.JWTToken;
import com.huydong.daotao.service.dto.UserInfo;
import java.util.Optional;

public interface AuthService {
    Optional<JWTToken> getAccessTokenFromCode(String code);
    Optional<UserInfo> getUserInfo(String accessToken);
    String getLogOutUri(String idToken);
}
