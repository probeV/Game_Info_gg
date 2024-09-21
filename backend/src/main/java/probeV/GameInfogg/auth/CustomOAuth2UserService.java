package probeV.GameInfogg.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import probeV.GameInfogg.auth.dto.request.OAuth2RequestDto;
import probeV.GameInfogg.repository.user.UserRepository;
import probeV.GameInfogg.domain.user.User;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("로그인 성공 및 유저 저장");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest); // OAuth2 정보를 가져옴

        // OAuth2User의 attribute
        Map<String, Object> originAttributes = oAuth2User.getAttributes();
        // OAuth2 서비스 ID(kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 소셜 정보를 가져옴
        // OAuth를 지원하는 소셜 서비스들간의 약속(google=sub, naver=id, kakako=response)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName(); // 해당 소셜 서비스에서 유니크한 id값을 전달

        log.info("userNameAttributeName : {}", userNameAttributeName);

        // OAuth2RequestDto 생성
        OAuth2RequestDto oAuth2RequestDto = OAuth2RequestDto.of(originAttributes, userNameAttributeName, registrationId);

        
        // 서버로 부터 가져온 속성값들을 기반으로 Save or Update
        User user = saveOrUpdate(oAuth2RequestDto);

        return new DefaultOAuth2User(
                Collections.singleton(
                        new SimpleGrantedAuthority(user.getRoleType().toString())),
                        originAttributes,
                        userNameAttributeName
        );      
    }


    private User saveOrUpdate(OAuth2RequestDto oAuth2RequestDto) {
        User user = userRepository.findByProviderAndAttributeCode(oAuth2RequestDto.getProvider(), oAuth2RequestDto.getAttributeCode())
                .map(entity -> entity.update(oAuth2RequestDto.getName(), oAuth2RequestDto.getEmail()))
                .orElse(oAuth2RequestDto.toEntity());

        return userRepository.save(user);
    }
}