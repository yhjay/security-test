package com.my.securitytest.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true) // 表示开启表达式验证注解
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator;
    /**
     * 配置密码加密器
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("test");
        // entryPoint 注入自定义异常处理
        OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
        entryPoint.setExceptionTranslator(customWebResponseExceptionTranslator);
        resources.authenticationEntryPoint(entryPoint);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.NEVER);


        // 设置不需要认证的rul
        http.authorizeRequests()
                .antMatchers("/index.html","/getUser2",
                        "/v2/**",
                        "/webjars/**",
                        "/doc.html",
                        "/swagger-resources/**",
                        "/favicon.ico",
                        "/*.js")
                .permitAll();

        // 其他任何请求需要认证
        http.authorizeRequests()
                .anyRequest().authenticated();

        // 支持跨域
        http.cors().and().csrf().disable();
    }



}
