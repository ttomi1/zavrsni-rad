package hr.tvz.zirdum.zavrsnibackend.dto;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}