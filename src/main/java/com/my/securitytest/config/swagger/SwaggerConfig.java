package com.my.securitytest.config.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.my.securitytest.model.ErrorMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @program: solar
 * @description: swagger配置
 * @author: Thomas.Yang
 * @create: 2018-12-19 08:47
 **/
@Configuration
@EnableSwagger2
@EnableKnife4j
public class SwaggerConfig {

    @Autowired
    private TypeResolver typeResolver;


    @Bean
    public Docket createRestApi() {

        List<ResponseMessage> responseMessageList = new ArrayList<>();
        responseMessageList.add(new ResponseMessageBuilder().code(401).message("需要授权认证").responseModel(new ModelRef("ErrorMsg")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(403).message("权限不足").responseModel(new ModelRef("ErrorMsg")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(404).message("找不到资源").responseModel(new ModelRef("ErrorMsg")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(409).message("业务逻辑异常").responseModel(new ModelRef("ErrorMsg")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(422).message("参数校验异常").responseModel(new ModelRef("ErrorMsg")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(500).message("服务器内部错误").responseModel(new ModelRef("ErrorMsg")).build());

        // 全局访问头
        ParameterBuilder pb = new ParameterBuilder();
        Parameter p1 =  pb.name("Authorization")
                .description("访问令牌")
                .defaultValue("Bearer ")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build();

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.my.securitytest"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(Collections.singletonList(p1))
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .additionalModels(typeResolver.resolve(ErrorMsg.class),
                        typeResolver.resolve(SwaggerAddtion.TokenModel.class));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("security测试")
                .description("powered by Thomas.Yang")
                .version("1.0")
                .build();
    }

}
