package com.lingjun.springsecuritypractice.security.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JwtParametric {

    public static final Long expirationTime = Long.valueOf(7200);

    public static final String secret = "AdminSecret";

    public static final String headerType = "Bearer";

    public static final String subject = "token";

    public static final String headerString = "token";

    public static final String CLAIM_KEY_USERNAME = "username";

    public static final String CLAIM_KEY_CREATED = "created";

    public static final String CLAIM_KEY_ROLE = "role";
}
