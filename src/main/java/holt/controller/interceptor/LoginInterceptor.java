package holt.controller.interceptor;

import holt.uitl.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * For each request defined in WebMvcConfig, validate the user's JWT
 * @author Weiyang Wu
 * @date 2024/7/19 23:31
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {


    /**
     * For each of the request to the defined route in Web config, check if its JWT is valid
     * @param request Http request
     * @param response Http response
     * @param handler Handler object
     * @return True if the JWT exists and is valid
     * @throws Exception Invalid JWT exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // Check if the JWT is a bearer JWT
        final String headerStarter = "Bearer ";
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith(headerStarter)) {
            String token = authHeader.substring(headerStarter.length());
            Claims claims = JwtUtil.parseToken(token);
            boolean isValidToken = JwtUtil.validateToken(claims);
            if (!isValidToken) {
                throw new Exception("Invalid JWT");
            }
            request.setAttribute("username", claims.getSubject());
            request.setAttribute("id", claims.getId());
        }
        return true;
    }
}
