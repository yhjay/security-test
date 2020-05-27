package com.my.securitytest.config.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;
import com.my.securitytest.config.security.AuthorizationServerConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.*;

@Component
public class SwaggerAddtion implements ApiListingScannerPlugin {
    @Override
    public List<ApiDescription> apply(DocumentationContext documentationContext) {

        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new ParameterBuilder()
                .description("oauth2 客户端 client_id、client_secret 字符串，格式 \"Basic Base64(client_id:client_secret)\"")//参数描述
                .type(new TypeResolver().resolve(String.class))//参数数据类型
                .parameterType("header")
                .name("Authorization")//参数名称
                .defaultValue("Basic d2ViOjEyMzQ1Ng==")//参数默认值
                .parameterAccess("access")
                .required(true)//是否必填
                .modelRef(new ModelRef("string")) //参数数据类型
                .build());
        parameters.add(new ParameterBuilder()
                .description("oauth2鉴权方式，password 或 refresh_token")//参数描述
                .type(new TypeResolver().resolve(String.class))//参数数据类型
                .name("grant_type")//参数名称
                .defaultValue("password")//参数默认值
                .parameterType("query")//参数类型
                .parameterAccess("access")
                .required(true)//是否必填
                .modelRef(new ModelRef("string")) //参数数据类型
                .build());
        parameters.add(new ParameterBuilder()
                .description("用户名")
                .type(new TypeResolver().resolve(String.class))
                .name("username")
                .parameterType("query")
                .parameterAccess("access")
                .required(false)
                .modelRef(new ModelRef("string")) //<5>
                .build());
        parameters.add(new ParameterBuilder()
                .description("密码")
                .type(new TypeResolver().resolve(String.class))
                .name("password")
                .parameterType("query")
                .parameterAccess("access")
                .required(false)
                .modelRef(new ModelRef("string")) //<5>
                .build());
        parameters.add(new ParameterBuilder()
                .description("刷新token")
                .type(new TypeResolver().resolve(String.class))
                .name("refresh_token")
                .parameterType("query")
                .parameterAccess("access")
                .required(false)
                .modelRef(new ModelRef("string")) //<5>
                .build());

        Set<ResponseMessage> responseMessages = new HashSet<>();
        responseMessages.add(new ResponseMessageBuilder().code(200).message("OK").responseModel(new ModelRef("TokenModel")).build());
        responseMessages.add(new ResponseMessageBuilder().code(401).message("需要授权认证").responseModel(new ModelRef("ErrorMsg")).build());
        responseMessages.add(new ResponseMessageBuilder().code(403).message("权限不足").responseModel(new ModelRef("ErrorMsg")).build());
        responseMessages.add(new ResponseMessageBuilder().code(404).message("找不到资源").responseModel(new ModelRef("ErrorMsg")).build());
        responseMessages.add(new ResponseMessageBuilder().code(409).message("业务逻辑异常").responseModel(new ModelRef("ErrorMsg")).build());
        responseMessages.add(new ResponseMessageBuilder().code(422).message("参数校验异常").responseModel(new ModelRef("ErrorMsg")).build());
        responseMessages.add(new ResponseMessageBuilder().code(500).message("服务器内部错误").responseModel(new ModelRef("ErrorMsg")).build());


        // @formatter:off
        return new ArrayList<>(Collections.singletonList(

                new ApiDescription("",
                        AuthorizationServerConfig.tokenPath,  //url
                        "UserToken", //描述
                        Collections.singletonList(
                                new OperationBuilder(
                                        new CachingOperationNameGenerator())
                                        .method(HttpMethod.POST)//http请求类型
                                        .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                                        .summary("登录、获取token")
                                        .notes("获取token")//方法描述
                                        .tags(Sets.newHashSet("用户登录相关"))//归类标签
                                        .parameters(parameters)
                                        .responseMessages(responseMessages)
                                        .build()),
                        false)));
        // @formatter:on
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType);
    }

    /**
     * 仅用于文档显示
     */
    @ApiModel("TokenModel")
    @Data
    public static class TokenModel {
        @ApiModelProperty(value = "access_token")
        private String access_token;
        @ApiModelProperty(value = "token 类型，默认 bearer")
        private String token_type;
        @ApiModelProperty(value = "有效期（秒），默认2小时")
        private String expires_in;
        @ApiModelProperty(value = "scope，当前默认 all")
        private String scope;
        @ApiModelProperty(value = "刷新token，有效期默认 1个月")
        private String refresh_token;
    }
}
