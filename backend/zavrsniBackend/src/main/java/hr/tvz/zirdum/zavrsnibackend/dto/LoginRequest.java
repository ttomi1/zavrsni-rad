package hr.tvz.zirdum.zavrsnibackend.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}