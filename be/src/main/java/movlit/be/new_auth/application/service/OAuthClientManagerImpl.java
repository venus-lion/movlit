//package movlit.be.new_auth.application.service;
//
//import lombok.RequiredArgsConstructor;
//import movlit.be.common.auth.AuthProvider;
//import movlit.be.new_auth.infra.client.GoogleClient;
//import movlit.be.new_auth.infra.client.OAuthClient;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class OAuthClientManagerImpl implements OAuthClientManager {
//
//    private final GoogleClient googleClient;
//
//    @Override
//    public OAuthClient getByProviderType(AuthProvider providerType) {
//        // FIXME OAuthClientRepository 구현 후 providerType에 맞는 web client를 반환하도록 수정
//        return googleClient;
//    }
//
//}
