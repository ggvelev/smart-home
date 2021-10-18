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

package com.iot.smarthome.security;

import com.iot.smarthome.security.jwt.JwtAuthenticationToken;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Guards the annotated REST controller handler method allowing either 'ROLE_ADMIN' user or the currently authenticated
 * principal to access own resources (when there's a '{userId}' path variable in the URI its value should match the one
 * in {@link JwtAuthenticationToken#getPrincipal()}.getUuid()).
 * <p>
 * Examples:
 * <li> GET /api/users/123 , the authenticated principal has id=123 -> authorized
 * <li> GET /api/users/123 , the authenticated principal has 'ROLE-ADMIN' -> authorized
 * <li> GET /api/users/123 , the authenticated principal has id=456 -> unauthorized, access to resource is forbidden
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("(#userId == principal.uuid.toString()) or principal.authorities.contains('ROLE_ADMIN')")
public @interface RequestingUserOrAdminAllowed {
}
