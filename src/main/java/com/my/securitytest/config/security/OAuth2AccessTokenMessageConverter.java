package com.my.securitytest.config.security;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 使用 fastjson 需要使用此 HttpMessageConverter
 */
public class OAuth2AccessTokenMessageConverter extends AbstractHttpMessageConverter<OAuth2AccessToken> {


    private final FastJsonHttpMessageConverter delegateMessageConverter;

    public OAuth2AccessTokenMessageConverter() {
        super(MediaType.APPLICATION_JSON);
        this.delegateMessageConverter = new FastJsonHttpMessageConverter();
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return OAuth2AccessToken.class.isAssignableFrom(clazz);
    }

    @Override
    protected OAuth2AccessToken readInternal(Class<? extends OAuth2AccessToken> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        throw new UnsupportedOperationException(
                "This converter is only used for converting from externally aqcuired form data");
    }

    @Override
    protected void writeInternal(OAuth2AccessToken accessToken, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        Map<String, Object> data = new HashMap<>();
        data.put(OAuth2AccessToken.ACCESS_TOKEN, accessToken.getValue());
        data.put(OAuth2AccessToken.TOKEN_TYPE, accessToken.getTokenType());
        data.put(OAuth2AccessToken.EXPIRES_IN, accessToken.getExpiresIn());
        data.put(OAuth2AccessToken.SCOPE, String.join(" ", accessToken.getScope()));
        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
        if (Objects.nonNull(refreshToken)) {
            data.put(OAuth2AccessToken.REFRESH_TOKEN, refreshToken.getValue());
        }
//        Map<String, Object> additionalInformation =accessToken.getAdditionalInformation();
//        if(!CollectionUtils.isEmpty(additionalInformation)){
//            additionalInformation.forEach(data::put);
//        }
        delegateMessageConverter.write(data, MediaType.APPLICATION_JSON, outputMessage);
    }
}


