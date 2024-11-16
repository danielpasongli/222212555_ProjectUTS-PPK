package com.polstat.helpdesk.controller;

import com.polstat.helpdesk.dto.UserProfileUpdateRequest;
import com.polstat.helpdesk.dto.ChangePasswordRequest;
import com.polstat.helpdesk.model.User;
import com.polstat.helpdesk.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Get User Profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User profile retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404",
                    description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - requires valid JWT token",
                    content = @Content)
    })
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserProfile(userDetails.getUsername());

        // Menampilkan role pengguna (sekarang hanya satu role)
        System.out.println("Role: " + user.getRole().getName());
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Update User Profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User profile updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid data provided",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - requires valid JWT token",
                    content = @Content)
    })
    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestBody UserProfileUpdateRequest userProfileUpdateRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User updatedUser = userService.updateUserProfile(userDetails.getUsername(), userProfileUpdateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Change User Password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Password updated successfully",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Old password is incorrect",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - requires valid JWT token",
                    content = @Content)
    })
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.changePassword(userDetails.getUsername(), changePasswordRequest);
        return ResponseEntity.ok().body("Password updated successfully");
    }

    @Operation(summary = "Delete User Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Account deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - requires valid JWT token",
                    content = @Content)
    })
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount() {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userDetails.getUsername();
            userService.deleteAccount(username);
            return ResponseEntity.ok().body("Account deleted successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body("User not found");
        }
    }
}
