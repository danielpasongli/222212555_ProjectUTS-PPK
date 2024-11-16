package com.polstat.helpdesk.service;

import com.polstat.helpdesk.model.Role;
import com.polstat.helpdesk.model.User;
import com.polstat.helpdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Mengonversi Role ke Collection<GrantedAuthority>
        Collection<GrantedAuthority> authorities = mapRoleToAuthority(user.getRole());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }

    // Ubah method ini untuk mendukung satu role
    private Collection<GrantedAuthority> mapRoleToAuthority(Role role) {
        // Mengonversi role tunggal ke dalam bentuk Collection<GrantedAuthority>
        return Set.of(new SimpleGrantedAuthority(role.getName()));
    }
}