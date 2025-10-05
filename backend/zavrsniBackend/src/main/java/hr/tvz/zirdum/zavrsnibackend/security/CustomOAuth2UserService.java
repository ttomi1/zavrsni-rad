package hr.tvz.zirdum.zavrsnibackend.security;

import hr.tvz.zirdum.zavrsnibackend.model.User;
import hr.tvz.zirdum.zavrsnibackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
@Primary

public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        System.out.println("✅ CustomOAuth2UserService konstruktor aktivan");
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        System.out.println("OAuth2 user service se pokrenuo");

        String email;
        String userName;
        String firstName = null;
        String lastName = null;
        String imageUrl = null;

        // mapiranje
        if (registrationId.equals("google")) {
            email = oAuth2User.getAttribute("email");
            userName = oAuth2User.getAttribute("name");
            firstName = oAuth2User.getAttribute("given_name");
            lastName = oAuth2User.getAttribute("family_name");
            imageUrl = oAuth2User.getAttribute("picture");
        } else if (registrationId.equals("github")) {
            email = oAuth2User.getAttribute("email");
            userName = oAuth2User.getAttribute("login");
            imageUrl = oAuth2User.getAttribute("avatar_url");

            String fullName = oAuth2User.getAttribute("name");
            if (fullName != null && fullName.contains(" ")) {
                String[] parts = fullName.split(" ");
                firstName = parts[0];
                lastName = parts.length > 1 ? parts[1] : null;
            } else {
                firstName = fullName;
            }
        } else {
            throw new OAuth2AuthenticationException("Nepodržani provider: " + registrationId);
        }

        // postoji li korisnik
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = User.builder()
                    .email(email)
                    .userName(userName)
                    .firstName(firstName)
                    .lastName(lastName)
                    .imageUrl(imageUrl)
                    .provider(registrationId.toUpperCase())
                    .roles(Set.of("USER"))
                    .build();

            user = userRepository.save(user);
        }

        // vrati korisnika Spring Security sustavu
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                "email"
        );
    }
}