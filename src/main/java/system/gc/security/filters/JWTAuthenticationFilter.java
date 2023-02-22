package system.gc.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import system.gc.dtos.ErrorDTO;
import system.gc.dtos.ErrorInfoDTO;
import system.gc.security.jwt.CreateTokenSuccessFulAuthentication;
import system.gc.utils.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter implements CreateTokenSuccessFulAuthentication {

    public JWTAuthenticationFilter(ProviderManager providerManager)
    {
        this.setAuthenticationManager(providerManager);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(TextUtils.GSON.toJson(new ErrorInfoDTO(new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), failed.getMessage()))));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

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
