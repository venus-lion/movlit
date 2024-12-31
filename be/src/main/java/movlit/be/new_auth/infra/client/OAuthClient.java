package movlit.be.new_auth.infra.client;

import movlit.be.new_auth.application.dto.OAuthMemberDetails;
import movlit.be.new_auth.infra.dto.OAuthTokenResponse;

public interface OAuthClient {

    OAuthMemberDetails getOAuthMemberDetails(String accessToken);

    OAuthTokenResponse getOauthToken(String authorizationCode);

}
