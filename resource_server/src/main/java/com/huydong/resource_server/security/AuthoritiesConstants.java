package com.huydong.resource_server.security;

import org.bouncycastle.pqc.crypto.newhope.NHSecretKeyProcessor;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String ADDRESS = "address";
    public static final String SUBJECT = "subject";
    public static final String RELATION = "relation";


    private AuthoritiesConstants() {}
}
