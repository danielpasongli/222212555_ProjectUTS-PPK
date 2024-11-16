package com.polstat.helpdesk.dto;

import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    private String username;
    private String email;
    private String role;  // Nama role yang ingin diubah
}
