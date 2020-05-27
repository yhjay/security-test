package com.my.securitytest.util;

import com.my.securitytest.model.UserInfo;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * 用于获取当前登录的用户信息
 */
public class SecurityUtil {

    public static UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(null == authentication){
            return null;
        }

        if(authentication instanceof AnonymousAuthenticationToken){
            return null;
        }

        if(authentication instanceof OAuth2Authentication){
            return (UserInfo) authentication.getPrincipal();
        }

        return null;
    }

}
