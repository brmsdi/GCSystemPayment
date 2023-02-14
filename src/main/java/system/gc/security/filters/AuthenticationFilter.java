package system.gc.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import system.gc.security.jwt.CreateTokenSuccessFulAuthentication;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter implements CreateTokenSuccessFulAuthentication {
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("USERNAME", authResult.getName());
            createTokenSuccessFulAuthentication(response, params);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
