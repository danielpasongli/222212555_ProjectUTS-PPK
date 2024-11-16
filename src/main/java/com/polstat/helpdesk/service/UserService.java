package com.polstat.helpdesk.service;

import com.polstat.helpdesk.dto.ChangePasswordRequest;
import com.polstat.helpdesk.dto.RegisterRequest;
import com.polstat.helpdesk.dto.UserProfileUpdateRequest;
import com.polstat.helpdesk.model.Role;
import com.polstat.helpdesk.model.User;
import com.polstat.helpdesk.repository.RoleRepository;
import com.polstat.helpdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User registerNewUser(RegisterRequest registerRequest) {
        // Membuat instance User baru dari request
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Menetapkan role default "ROLE_MAHASISWA"
        Role mahasiswaRole = roleRepository.findByName("ROLE_MAHASISWA")
                .orElseThrow(() -> new RuntimeException("Error: Role ROLE_MAHASISWA not found."));

        // Menambahkan role ke user (pastikan hanya satu role per user)
        user.setRole(mahasiswaRole);

        // Menyimpan user ke dalam database
        return userRepository.save(user);
    }


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public Optional<User> findByUsernameWithRoles(String username) {
        return userRepository.findByUsernameWithRoles(username);
    }

    public User getUserProfile(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUserProfile(String currentUsername, UserProfileUpdateRequest request) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            user.setEmail(request.getEmail());
        }

        // Update the user profile in the database
        return userRepository.save(user);
    }

    public void changePassword(String username, ChangePasswordRequest request) {
        User user = getUserProfile(username);
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteAccount(String username) {
        // Cek apakah user dengan username ada di database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Memutuskan hubungan user dengan role, set role menjadi null
        if (user.getRole() != null) {
            user.setRole(null);  // Memutuskan hubungan role dengan user
            userRepository.save(user);  // Simpan perubahan tersebut
        }

        // Menghapus user dari database
        userRepository.delete(user);
    }

}
