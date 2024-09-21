package probeV.GameInfogg.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class SecurityUtil {


    public static String getAttributeCode() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        //log.info("principal : {}", principal);

        return principal.getUsername();
    }
}