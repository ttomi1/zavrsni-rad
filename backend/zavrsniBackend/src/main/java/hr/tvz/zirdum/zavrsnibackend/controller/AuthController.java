package hr.tvz.zirdum.zavrsnibackend.controller;

import hr.tvz.zirdum.zavrsnibackend.dto.JwtAuthResponse;
import hr.tvz.zirdum.zavrsnibackend.dto.LoginRequest;
import hr.tvz.zirdum.zavrsnibackend.dto.RegisterRequest;
import hr.tvz.zirdum.zavrsnibackend.dto.TokenRefreshRequest;
import hr.tvz.zirdum.zavrsnibackend.model.RefreshToken;
import hr.tvz.zirdum.zavrsnibackend.model.User;
import hr.tvz.zirdum.zavrsnibackend.repository.UserRepository;
import hr.tvz.zirdum.zavrsnibackend.service.JwtService;
import hr.tvz.zirdum.zavrsnibackend.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwtToken = jwtService.generateToken(loginRequest.getEmail());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.getEmail());

            // refresh token HttpOnly cookie
            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60) // 7 dana
                    .sameSite("Lax")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new JwtAuthResponse(jwtToken, null));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Pogrešni podaci");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Refresh token nije pronađen."));
        }

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(rt -> {
                    String accessToken = jwtService.generateToken(rt.getUserInfo().getEmail());
                    return ResponseEntity.ok(Map.of("accessToken", accessToken));
                })
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "Nevažeći token")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Email već postoji");
        }

        User user = new User();
        user.setUserName(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRoles(Set.of("USER"));

        userRepository.save(user);

        return ResponseEntity.ok("Registracija uspješna");
    }


}