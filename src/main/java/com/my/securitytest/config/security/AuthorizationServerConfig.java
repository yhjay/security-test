package com.my.securitytest.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.concurrent.TimeUnit;

@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    public static final String tokenPath = "/my/oauth/token";

    @Autowired
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator;


    public AuthorizationServerConfig(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // @formatter:off
        clients.inMemory()
                .withClient("lamp")
                .secret(passwordEncoder.encode("lamp123"))
                .scopes("all")
                .resourceIds("test")
                .authorizedGrantTypes("password", "refresh_token")
                // access_token 失效时间2小时
                .accessTokenValiditySeconds((int)TimeUnit.HOURS.toSeconds(2))
                // refresh_token 失效时间30天
                .refreshTokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(30));
        // @formatter:on
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .tokenStore(new JwtTokenStore(accessTokenConverter()))
                .accessTokenConverter(accessTokenConverter())
                // 支持get 和 post 提交
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                // 用于 refresh_token
                .userDetailsService(customUserDetailsService)
                // 修改获取token地址
                .pathMapping("/oauth/token",tokenPath)
                .exceptionTranslator(customWebResponseExceptionTranslator);

    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // entryPoint 注入自定义异常处理
        OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
        entryPoint.setExceptionTranslator(customWebResponseExceptionTranslator);
        security.authenticationEntryPoint(entryPoint);
        // 解决token获取接口的跨域问题
        CorsFilter filter = new CorsFilter(corsConfigurationSource());
        security.addTokenEndpointAuthenticationFilter(filter);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        // 设置jwt对称密钥
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("koyoe123");
        // 自定义tokenConverter 用于给jwt中添加自定义扩展
        ((DefaultAccessTokenConverter)converter
                .getAccessTokenConverter()).setUserTokenConverter(new CustomUserAuthenticationConverter());
        return converter;
    }

}



