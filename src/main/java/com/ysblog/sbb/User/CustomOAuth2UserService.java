package com.ysblog.sbb.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = getUserEmail(oAuth2User, userRequest.getClientRegistration().getRegistrationId());
        String name = getUserName(oAuth2User, userRequest.getClientRegistration().getRegistrationId());
        String username = getUserId(oAuth2User, userRequest.getClientRegistration().getRegistrationId());

        Optional<SiteUser> userOptional = this.userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            SiteUser user = userOptional.get();
            user.setNickname(name);
            this.userRepository.save(user);
        } else {
            SiteUser newUser = new SiteUser();
            newUser.setEmail(email);
            newUser.setUsername(username);
            newUser.setNickname(name);
            newUser.setJoinDate(LocalDateTime.now());
            this.userRepository.save(newUser);
        }

        return oAuth2User;
    }

    private String getUserEmail(OAuth2User oAuth2User, String registrationId) {
        if ("google".equals(registrationId)) {
            return oAuth2User.getAttribute("email");
        } else if ("naver".equals(registrationId)) {
            Map<String, Object> naverAttributes = oAuth2User.getAttribute("attributes");
            if (naverAttributes != null) {
                Map<String, Object> naverResponse = (Map<String, Object>) naverAttributes.get("response");
                if (naverResponse != null) {
                    return (String) naverResponse.get("email");
                }
            }
        } else if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAttributes = oAuth2User.getAttribute("kakao_account");
            if (kakaoAttributes != null) {
                return (String) kakaoAttributes.get("email");
            }
        }
        return null;
    }

    private String getUserName(OAuth2User oAuth2User, String registrationId) {
        if ("google".equals(registrationId)) {
            return oAuth2User.getAttribute("name");
        } else if ("naver".equals(registrationId)) {
            Map<String, Object> naverAttributes = oAuth2User.getAttribute("attributes");
            if (naverAttributes != null) {
                Map<String, Object> naverResponse = (Map<String, Object>) naverAttributes.get("response");
                if (naverResponse != null) {
                    return (String) naverResponse.get("name");
                }
            }
        } else if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAttributes = oAuth2User.getAttribute("properties");
            if (kakaoAttributes != null) {
                return (String) kakaoAttributes.get("nickname");
            }
        }
        return null;
    }

    private String getUserId(OAuth2User oAuth2User, String registrationId) {
        if ("google".equals(registrationId)) {
            return oAuth2User.getAttribute("sub");
        } else if ("naver".equals(registrationId)) {
            Map<String, Object> naverAttributes = oAuth2User.getAttribute("attributes");
            if (naverAttributes != null) {
                Map<String, Object> naverResponse = (Map<String, Object>) naverAttributes.get("response");
                if (naverResponse != null) {
                    return (String) naverResponse.get("id");
                }
            }
        } else if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAttributes = oAuth2User.getAttribute("kakao_account");
            if (kakaoAttributes != null) {
                System.out.println("id =" + (String) kakaoAttributes.get("id"));
            }
        }
        return null;
    }
}
