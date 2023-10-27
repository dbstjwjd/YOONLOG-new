package com.ysblog.sbb.User;

import com.ysblog.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void createUser(String username, String nickname, String password, String email, String address, LocalDate birthDate) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setNickname(nickname);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setAddress(address);
        user.setBirthDate(birthDate);
        user.setJoinDate(LocalDateTime.now());
        this.userRepository.save(user);
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("user not found");
        }
    }

    public void modifyUser(SiteUser user, String nickname, LocalDate birthDate, String address, String email) {
        user.setNickname(nickname);
        user.setBirthDate(birthDate);
        user.setAddress(address);
        user.setEmail(email);
        this.userRepository.save(user);
    }
}
