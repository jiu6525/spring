package springsecurity.springsecurity.config.oauth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import springsecurity.springsecurity.config.CustomBCryptPasswordEncoder;
import springsecurity.springsecurity.config.auth.PrincipalDetails;
import springsecurity.springsecurity.config.auth.provider.*;
import springsecurity.springsecurity.model.User;
import springsecurity.springsecurity.repository.UserRepository;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private CustomBCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); //registrationId로 어떤 OAuth로 로그인했는지 확인
        System.out.println("getAccessToken : " + userRequest.getAccessToken());

        OAuth2User oauth2User = super.loadUser(userRequest);
        // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인을 완료 -> code 를 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
        // userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원 프로필 받아준다.
        System.out.println("getAttributes : " + oauth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = null;
        OAuth2UserInfo oAuth2UserInfoKakao = null;
        // 회원가입을 강제로 진행할 예정
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FaceBookUserInfo(oauth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")){
            System.out.println("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo((Map)oauth2User.getAttributes().get("kakao_account"));
            oAuth2UserInfoKakao = new KakaoUserInfo(oauth2User.getAttributes());
        } else{
            System.out.println("우리는 구글,페이스북,네이버만 지원합니다.");
        }
        String provider, providerId, username, password, email, role = "";

        provider = oAuth2UserInfo.getProvider();// google
        if(oAuth2UserInfoKakao == null){
            providerId = oAuth2UserInfo.getProviderId();
        }else{
            providerId = oAuth2UserInfoKakao.getProviderId();
        }
        username = provider + "_" + providerId;
        password = bCryptPasswordEncoder.encode("겟인데어");
        email = oAuth2UserInfo.getEmail();
        role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            System.out.println("OAuth 로그인이 최초입니다.");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            userRepository.save(userEntity);
        }else{
            System.out.println("로그인을 이미 한적이 있습니다. 당신은 자동회원가입이 되어 있습니다. ");
        }

        return new PrincipalDetails(userEntity, oauth2User.getAttributes());
    }
}
