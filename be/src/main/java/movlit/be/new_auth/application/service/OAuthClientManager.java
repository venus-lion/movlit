package movlit.be.new_auth.application.service;

import movlit.be.common.auth.AuthProvider;
import movlit.be.new_auth.infra.client.OAuthClient;

public interface OAuthClientManager {

    OAuthClient getByProviderType(AuthProvider providerType);

}
