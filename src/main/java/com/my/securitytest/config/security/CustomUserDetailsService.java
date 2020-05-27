package com.my.securitytest.config.security;

import com.my.securitytest.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // TODO: 此处需要从数据库查询用户信息
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if("apple".equalsIgnoreCase(s)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return new UserInfo("1","apple", passwordEncoder.encode("123456"), authorities);
        }else if("pair".equalsIgnoreCase(s)){
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            return new UserInfo("2","pair", passwordEncoder.encode("123456"), authorities);
        }
        throw new UsernameNotFoundException("用户名或密码不正确");
    }

}
