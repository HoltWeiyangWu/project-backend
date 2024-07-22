package holt.config;

import holt.controller.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Assign configuration for login interceptor
 * @author Weiyang Wu
 * @date 2024/7/22 8:19
 */

@Component
public class WebMvcConfig implements WebMvcConfigurer {
    private LoginInterceptor loginInterceptor;

    /**
     * Constructor to bind the interceptor bean for the configuration
     * @param loginInterceptor Login interceptor
     */
    @Autowired
    public WebMvcConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    /**
     * Configures paths to the interceptor, except for the login and the signup pages
     * @param registry Interceptor registry to record the interceptor instance
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/user/login", "/user/register")
                .addPathPatterns("/**");
    }
}
