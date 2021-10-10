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
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolver for Bearer token authorization header
 */
public final class AuthorizationHeaderBearerTokenResolver {

    private static final Pattern authorizationPattern = Pattern.compile(
            "^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$",
            Pattern.CASE_INSENSITIVE
    );

    private final String bearerTokenHeaderName;

    /**
     * Constructs a {@link AuthorizationHeaderBearerTokenResolver} with default name of {@link
     * HttpHeaders#AUTHORIZATION} for the header containing the Bearer token.
     */
    public AuthorizationHeaderBearerTokenResolver() {
        this(HttpHeaders.AUTHORIZATION);
    }

    /**
     * Constructs a {@link AuthorizationHeaderBearerTokenResolver} with the specified header name that should contain
     * the Bearer token.
     * <p>
     * This allows other headers to be used as the Bearer Token source such as {@link HttpHeaders#PROXY_AUTHORIZATION}
     * or any custom one (e.g. "X-Auth-Token", "X-Smarthome-API-Token" etc.)
     *
     * @param bearerTokenHeaderName the header to check when retrieving the Bearer Token.
     */
    public AuthorizationHeaderBearerTokenResolver(String bearerTokenHeaderName) {
        Assert.hasText(bearerTokenHeaderName, "Authentication header cannot be empty");
        this.bearerTokenHeaderName = bearerTokenHeaderName;
    }

    /**
     * Attempts to resolve a Bearer authorization token from {@link HttpServletRequest}
     *
     * @param request the incoming HTTP request
     * @return Bearer token, if present, otherwise null
     */
    public String resolve(HttpServletRequest request) {
        String authorization = request.getHeader(bearerTokenHeaderName);

        if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
            return null;
        }

        Matcher matcher = authorizationPattern.matcher(authorization);
        if (!matcher.matches()) {
            throw new MalformedBearerTokenException("The provided Bearer token is malformed");
        }

        return matcher.group("token");
    }

}