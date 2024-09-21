package probeV.GameInfogg.auth;

import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class SecurityUtil {


    public static String getAttributeCode() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        //log.info("principal : {}", principal);

        return principal.getUsername();
    }
}