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

package com.iot.smarthome.config;

import com.iot.smarthome.security.jwt.JwtAuthenticationProvider;
import com.iot.smarthome.security.jwt.JwtTokenFilter;
import com.iot.smarthome.security.jwt.JwtTokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static com.iot.smarthome.controller.ApiConstants.*;
import static com.iot.smarthome.security.SecurityConstants.*;
import static org.springframework.http.HttpMethod.*;

/**
 * Application security (authentication and authorization) configuration
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private JwtTokenHandler jwtTokenHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new JwtAuthenticationProvider(jwtTokenHandler));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Stateless session management
        http = http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Permissions on endpoints
        http.authorizeRequests()
                // Allow anyone to authenticate, create/activate/recover account:
                .antMatchers(GET, AUTHENTICATION).permitAll()
                .antMatchers(POST, AUTHENTICATION).permitAll()
                .antMatchers(POST, USERS).permitAll()
                .antMatchers(POST, USER_RECOVERY).permitAll()

                .antMatchers("/**").permitAll()

                // Make access to Open API docs and SwaggerUI unrestricted: (TODO - allow only for ROLE_SWAGGER?)
                .antMatchers(OPEN_API_DOCS).permitAll()

                .antMatchers(GET, DEVICES).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                .antMatchers(POST, DEVICES).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)

                .antMatchers(GET, DEVICE_BY_ID).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                .antMatchers(PUT, DEVICE_BY_ID).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                .antMatchers(DELETE, DEVICE_BY_ID).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)

                .antMatchers(GET, DEVICE_METADATA).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                .antMatchers(PUT, DEVICE_METADATA).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)

                .antMatchers(GET, DEVICE_NETWORK_SETTINGS).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                .antMatchers(PUT, DEVICE_NETWORK_SETTINGS).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)

                .antMatchers(GET, DEVICE_PROPERTY_TYPES).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                .antMatchers(GET, DEVICE_PROPERTIES).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                .antMatchers(POST, DEVICE_PROPERTIES).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)

                .antMatchers(GET, DEVICE_PROPERTY_BY_ID).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                .antMatchers(PUT, DEVICE_PROPERTY_BY_ID).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                .antMatchers(DELETE, DEVICE_PROPERTY_BY_ID).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)

                .antMatchers(GET, DEVICE_CONFIGURATIONS).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                .antMatchers(POST, DEVICE_CONFIGURATIONS).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)

                .antMatchers(GET, DEVICE_CONFIGURATION_BY_ID).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                .antMatchers(PUT, DEVICE_CONFIGURATION_BY_ID).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                .antMatchers(DELETE, DEVICE_CONFIGURATION_BY_ID).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)

                .antMatchers(GET, DEVICE_STATE_HISTORY).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                .antMatchers(DELETE, DEVICE_STATE_HISTORY).hasAnyAuthority(ROLE_ADMIN, ROLE_USER)

                .antMatchers(GET, USERS).hasAnyAuthority(ROLE_ADMIN)

                .antMatchers(GET, USER_BY_ID).access(ADMIN_OR_AUTHENTICATED_USER_ACCESSIBLE)
                .antMatchers(PUT, USER_BY_ID).access(ADMIN_OR_AUTHENTICATED_USER_ACCESSIBLE)
                .antMatchers(DELETE, USER_BY_ID).access(ADMIN_OR_AUTHENTICATED_USER_ACCESSIBLE)

                .antMatchers(GET, USER_NOTIFICATION_SETTINGS).access(ADMIN_OR_AUTHENTICATED_USER_ACCESSIBLE)
                .antMatchers(PUT, USER_NOTIFICATION_SETTINGS).access(ADMIN_OR_AUTHENTICATED_USER_ACCESSIBLE)
                .antMatchers(DELETE, USER_NOTIFICATION_SETTINGS).access(ADMIN_OR_AUTHENTICATED_USER_ACCESSIBLE)

                // User must be authenticated (and not "remembered") for any other request
                .anyRequest().fullyAuthenticated()

                .and()
                .requiresChannel().anyRequest().requiresSecure()
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(false);
        config.addAllowedOrigin("https://localhost");
        config.addAllowedOrigin("http://localhost");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}