/***********************************
 Copyright (c) Georgi Velev 2021.
 **********************************/
package com.iot.smarthome.security.jwt;

import com.iot.smarthome.dto.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

import static com.iot.smarthome.security.jwt.JwtClaimConstants.EMAIL;
import static com.iot.smarthome.security.jwt.JwtClaimConstants.USER_UUID;

/**
 * Authentication provider based on JSON Web Tokens
 *
 * @see AuthenticationProvider
 * @see JwtTokenFilter
 * @see BearerAuthenticationToken
 * @see JwtAuthenticationToken
 */
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenHandler jwtHandler;

    public JwtAuthenticationProvider(JwtTokenHandler jwtHandler) {
        this.jwtHandler = jwtHandler;
    }

    /**
     * Decode and validate the
     * <a href="https://tools.ietf.org/html/rfc6750#section-1.2" target="_blank">Bearer
     * Token</a>.
     *
     * @param authentication the authentication request object.
     * @return A successful authentication
     * @throws AuthenticationException if authentication failed for some reason
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BearerAuthenticationToken bearer = (BearerAuthenticationToken) authentication;
        Jws<Claims> jwt = getJwt(bearer);

        // Create the successful authentication:
        final AbstractAuthenticationToken auth = createJwtAuthenticationToken(jwt);
        auth.setDetails(bearer.getDetails());
        log.info("Authenticated token {}", bearer.getTokenObscured());

        return auth;
    }

    /**
     * Extracts signed JWT from {@link BearerAuthenticationToken}
     *
     * @param bearer auth token containing the compact, signet JWT token
     * @return expanded/decoded JWS token
     */
    private Jws<Claims> getJwt(BearerAuthenticationToken bearer) {
        try {
            return this.jwtHandler.decode(bearer.getToken());
        } catch (JwtException failed) {
            log.error("Error on authentication with {}", bearer.getTokenObscured());
            throw new AuthenticationServiceException(failed.getMessage(), failed);
        }
    }

    /**
     * Constructs a {@link JwtAuthenticationToken} out of {@link Jws} instance
     *
     * @param jwt signed JWT
     * @return {@link JwtAuthenticationToken}
     */
    private JwtAuthenticationToken createJwtAuthenticationToken(Jws<Claims> jwt) {
        final Claims body = jwt.getBody();
        final String authoritiesClaim = body.get(JwtClaimConstants.AUTHORITIES, String.class);
        final List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim);

        // Populate details for the currently authenticated principal
        final UserDetails userDetails = new UserDetails(
                UUID.fromString(body.get(USER_UUID, String.class)),
                body.getSubject(),
                body.get(EMAIL, String.class),
                StringUtils.commaDelimitedListToSet(authoritiesClaim)
        );

        return new JwtAuthenticationToken(jwt, userDetails, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return BearerAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
