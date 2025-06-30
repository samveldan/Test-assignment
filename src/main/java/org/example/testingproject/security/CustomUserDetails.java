package org.example.testingproject.security;

import lombok.RequiredArgsConstructor;
import org.example.testingproject.models.Person;
import org.example.testingproject.models.PersonRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final Person person;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<PersonRole> list = person.getList();

        return list.stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getRole().getRoleName().name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return person.getUsername();
    }
}
