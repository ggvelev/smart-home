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

import com.iot.smarthome.exception.MalformedBearerTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A filter that authenticates an incoming {@link HttpServletRequest} by looking at the {@link
 * HttpHeaders#AUTHORIZATION} header for a Bearer token.
 *
 * @see org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final AuthorizationHeaderBearerTokenResolver bearerTokenResolver = new AuthorizationHeaderBearerTokenResolver();
    private final AuthenticationEntryPoint authenticationEntryPoint = (request, response, authException) -> {
        log.error("JWT authentication failed: {}", authException.getMessage());

        // The API client should check if response status is 401 as well as if there's
        // a header "Authenticate: Bearer" which states (JWT) authentication is required.
        response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    };
    @Autowired
    private JwtTokenVerifier jwtTokenVerifier;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Validate "Authorization" header and extract JWT token:
        String token;
        try {
            token = bearerTokenResolver.resolve(request);
        } catch (MalformedBearerTokenException e) {
            log.error("Sending to authentication entry point, bearer token resolution failed", e);
            authenticationEntryPoint.commence(request, response, e);
            return;
        }

        // Validate the token:
        if (token == null || !jwtTokenVerifier.verifyToken(token)) {
            log.info("Did not process request since no bearer token present");
            filterChain.doFilter(request, response);
            return;
        }

        // Wrap it and pass it to JwtAuthenticationProvider
        final BearerAuthenticationToken auth = new BearerAuthenticationToken(token);
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        try {
            final Authentication authResult = authenticationManager.authenticate(auth);
            final SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            log.debug("SecurityContextHolder set to {}", authResult);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, e);
            log.debug("Failed to process authentication request", e);
        }
    }

}
