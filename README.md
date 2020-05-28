# SpringBoot + SpringSecurity + OAuth2 密码模式Demo
+ 授权服务器和资源服务器配置在一个项目中。
+ 解决了使用FastJson后获取token响应格式不正确的问题。`OAuth2AccessTokenMessageConverter`
+ 添加了自定义异常处理类，异常转换为自定义描述。`CustomWebResponseExceptionTranslator`
+ 自定义了`UserAuthenticationConverter`,修改了`convertUserAuthentication`方法，在jwt的token中添加用户id字段，
修改了`extractAuthentication`方法，在没有配置userDetailsService的情况下，返回用户id和用户名的对象，而非只返回用户名。
+ 授权服务器和资源服务器同时添加了跨域支持。
+ 将token获取的接口，添加到swagger中，便于调试。

