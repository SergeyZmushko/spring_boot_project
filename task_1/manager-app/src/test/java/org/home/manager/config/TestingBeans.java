package org.home.manager.config;

import jakarta.servlet.Filter;
import org.home.manager.client.RestClientProductsRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.mock;

@Configuration
public class TestingBeans {

//    @Autowired
//    private Filter springSecurityFilterChain;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Bean
    public MockMvc mockMvc() {

        return MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
//                .apply(springSecurityFilterChain)
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return mock(ClientRegistrationRepository.class);
    }

    @Bean
    public OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository() {
        return mock(OAuth2AuthorizedClientRepository.class);
    }

    @Bean
//    @Primary
    public RestClientProductsRestClient testRestClientProductsRestClient(@Value("${home.service.catalogue.uri:http://localhost:54321}") String catalogBaseUri) {
        return new RestClientProductsRestClient(RestClient.builder()
                .baseUrl(catalogBaseUri).build());
    }
}
