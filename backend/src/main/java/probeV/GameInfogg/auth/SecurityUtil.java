package probeV.GameInfogg.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.extern.slf4j.Slf4j;
import probeV.GameInfogg.repository.user.UserRepository;

import org.springframework.stereotype.Component;
@Slf4j
@Component
public class SecurityUtil {
    private final UserRepository userRepository;

    public SecurityUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public probeV.GameInfogg.domain.user.User getUser() { // static 제거
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        probeV.GameInfogg.domain.user.User user = userRepository.findByAttributeCode(principal.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        return user;
    }
}