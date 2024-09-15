package org.example.pongguel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class ApiConfig {

    /*
    * WebClient의 UriComponentsBuilder.encode() 방식의 인코딩 방지
    * key 값이 달라지는 것을 방지하기 위해 DefaultUriBuilderFactory 생성
    * encoding 모드 지정
    * */
    @Bean
    public DefaultUriBuilderFactory builderFactory() {
        DefaultUriBuilderFactory builderFactory = new DefaultUriBuilderFactory();
        builderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        return builderFactory;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .uriBuilderFactory(builderFactory())
                .build();
    }
}
