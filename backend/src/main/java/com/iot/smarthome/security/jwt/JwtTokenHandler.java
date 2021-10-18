/*******************************************************************************
 * MIT License
 *
 * Copyright (c) 2021 Georgi Velev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 ******************************************************************************/

package com.iot.smarthome.security.jwt;

import com.iot.smarthome.entity.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

import static com.iot.smarthome.security.SecurityConstants.ROLE_ADMIN;
import static com.iot.smarthome.security.jwt.JwtClaimConstants.*;

/**
 * Component for generating and validating JWT tokens.
 */
@Component
public class JwtTokenHandler implements JwtTokenVerifier, JwtTokenGenerator {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenHandler.class);

    private JwtParser parser;

    /**
     * SecretKey instance for use with HMAC-SHA algorithms based on {@link #jwtSecret}.
     * <p>
     * The key is used for generating and validating/parsing signed JWTs (JWS).
     */
    private SecretKey signingKey;

    /**
     * Base64 encoded secret key
     */
    @Value("${jwt.token.secret}")
    private byte[] jwtSecret;

    /**
     * JWT token issuer "{@code iss}"
     */
    @Value("${jwt.token.issuer}")
    private String jwtIssuer;

    /**
     * JWT token expiration in seconds
     */
    @Value("${jwt.token.ttl-in-seconds}")
    private long tokenTtl;

    /**
     * Initializes JWT signing key and signed JWT (JWS) parser instance for later use
     */
    @PostConstruct
    private void init() {
        signingKey = Keys.hmacShaKeyFor(jwtSecret);
        parser = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .requireIssuer(jwtIssuer)
                .build();
    }

    @Override
    public String generateToken(UserEntity user) {
        final long iat = System.currentTimeMillis();

        return Jwts.builder()
                .signWith(signingKey)
                .setId(UUID.randomUUID().toString())
                .setSubject(user.getUsername())
                .setIssuer(jwtIssuer)
                // .setAudience("https://localhost/smart-home")
                .setIssuedAt(new Date(iat))
                .setNotBefore(new Date(iat - 2000))
                .setExpiration(new Date(iat + tokenTtl * 1000))
                .claim(AUTHORITIES, user.getAuthoritiesAsCommaSeparatedString())
                .claim(EMAIL, user.getEmail())
                .claim(USER_UUID, user.getUuid())
                .claim(ADMIN, user.hasAnyAuthority(ROLE_ADMIN))
                .compact();
    }

    /**
     * Parses the given JWT token and produces expanded signed JWT ({@link Jws}) instance
     *
     * @param token compact serialized JWT bearer token
     * @return the parsed signed JSON Web Token
     */
    public Jws<Claims> decode(String token) {
        return parser.parseClaimsJws(token);
    }

    @Override
    public boolean verifyToken(String token) {
        try {
            parser.parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature - {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Malformed JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (JwtException ex) {
            log.error("JWT validation error - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty - {}", ex.getMessage());
        }
        return false;
    }

}
