package com.test.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class CustomJwtRoleMapper implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    @Nullable
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // Extract the "resource_access" claim
        Map<String, Object> resourceAccess = jwt.getClaim("realm_access");
        if (resourceAccess != null) {
            // Get the client-specific map, here "saga"
            List<String> roles = (List<String>) resourceAccess.get("roles");
            // Map<String, Object> clientResource = (Map<String, Object>)
            // resourceAccess.get("roles");

            // Get roles from the client

            if (roles != null) {
                // Add each role as a SimpleGrantedAuthority (no need to add ROLE_ prefix
                // manually since JWT already contains it)
                roles.forEach(role -> authorities
                        .add(new SimpleGrantedAuthority(!role.startsWith("ROLE_") ? "ROLE_" + role : role)));
            }

        }
        return authorities;

    }

}
