package hr.tvz.zirdum.zavrsnibackend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.tvz.zirdum.zavrsnibackend.model.RefreshToken;
import hr.tvz.zirdum.zavrsnibackend.model.User;
import hr.tvz.zirdum.zavrsnibackend.repository.UserRepository;
import hr.tvz.zirdum.zavrsnibackend.service.JwtService;
import hr.tvz.zirdum.zavrsnibackend.service.RefreshTokenService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @Autowired
    public CustomOAuth2SuccessHandler(JwtService jwtService,
                                      RefreshTokenService refreshTokenService,
                                      UserRepository userRepository) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, java.io.IOException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();

        String email = (String) attributes.get("email");

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            System.out.println("Korisnik nije spremljen, CustomOAuth2UserService se nije pokrenuo");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "OAuth2 login failed");
            return;
        }

        User user = userOptional.get();

        String jwtToken = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        // refreshToken u HttpOnly cookie
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken.getToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/"); //svi endpointovi
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 dana

        response.addCookie(refreshCookie);

        // redirectaj s accessTokenom u URLu
        String redirectUrl = String.format("http://localhost:4200/oauth2-redirect?accessToken=%s", jwtToken);
        response.sendRedirect(redirectUrl);
    }
}