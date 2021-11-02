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

package com.iot.smarthome.validation;

import com.iot.smarthome.dto.CreateUserRequest;
import com.iot.smarthome.exception.InvalidEmailException;
import com.iot.smarthome.exception.PasswordStrengthException;
import com.iot.smarthome.exception.DuplicateUserException;
import com.iot.smarthome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Validates properties related to {@link com.iot.smarthome.entity.UserEntity}
 */
@Component
public class UserDetailsValidator {

    @Autowired
    private UserRepository userRepository;

    /**
     * This regex will enforce these rules:
     * <ul>
     * <li> At least one upper case English letter, <code>(?=.*?[A-Z])</code></li>
     * <li> At least one lower case English letter, <code>(?=.*?[a-z])</code> </li>
     * <li> At least one digit, <code>(?=.*?[0-9])</code> </li>
     * <li> At least one special character, <code>(?=.*?[#?!@$%^&*-])</code> </li>
     * <li> Minimum eight in length <code>.{8,}</code> (with the anchors) </li>
     * </ul>
     */
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Validates user registration request
     *
     * @param request the registration request
     */
    public void validateUserRegistrationRequest(CreateUserRequest request) {
        if (request.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username must not be blank");
        }

        if (!isPasswordStrongEnough(request.getPassword())) {
            throw new PasswordStrengthException();
        }

        if (!isEmailValid(request.getEmail())) {
            throw new InvalidEmailException(request.getEmail());
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUserException("username", request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("email", request.getEmail());
        }
    }

    private boolean isPasswordStrongEnough(String password) {
        return password != null && !password.isBlank() && PASSWORD_PATTERN.matcher(password).matches();
    }

    private boolean isEmailValid(String email) {
        return email != null && !email.isBlank() && EMAIL_PATTERN.matcher(email).matches();
    }

}
