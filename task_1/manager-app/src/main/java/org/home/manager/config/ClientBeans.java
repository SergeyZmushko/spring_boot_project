package org.home.manager.config;

import org.home.manager.client.RestClientProductsRestClient;
import org.home.manager.security.OAuthClientRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientBeans {

    @Bean
    public RestClientProductsRestClient productsRestClient(
            @Value("${home.service.catalogue.uri:http://localhost:8081}") String catalogBaseUri,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository,
            @Value("${home.service.catalogue.username:}") String catalogUsername,
            @Value("${home.service.catalogue.password:}") String catalogPassword,
            @Value("${home.service.catalogue.registration-id:keycloak}") String registrationId) {
        return new RestClientProductsRestClient(RestClient.builder()
                .baseUrl(catalogBaseUri)
                .requestInterceptor(new OAuthClientRequestInterceptor(
                        new DefaultOAuth2AuthorizedClientManager(
                                clientRegistrationRepository, authorizedClientRepository),
                        registrationId))
//                .requestInterceptor(new BasicAuthenticationInterceptor(catalogUsername, catalogPassword))
                .build());
    }
}
