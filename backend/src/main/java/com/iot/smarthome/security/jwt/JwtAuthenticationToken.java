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

import com.iot.smarthome.dto.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Transient;

import java.util.Collection;

/**
 * Constructed by {@link JwtAuthenticationProvider} upon successful authentication against JWT
 */
@Transient
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Jws<Claims> token;

    private final UserDetails principal;

    public JwtAuthenticationToken(Jws<Claims> token, UserDetails principal,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.principal = principal;
        super.setAuthenticated(true);
    }

    public Jws<Claims> getToken() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return getToken();
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        throw new UnsupportedOperationException(
                "Cannot call this method directly. Call this class' constructor instead.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        JwtAuthenticationToken that = (JwtAuthenticationToken) o;

        if (!token.equals(that.token)) {
            return false;
        }
        return principal.equals(that.principal);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + token.hashCode();
        result = 31 * result + principal.hashCode();
        return result;
    }
}
