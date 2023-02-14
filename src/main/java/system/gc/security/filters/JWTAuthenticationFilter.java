package system.gc.security.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import system.gc.security.UserModelDetailsService;
import system.gc.security.jwt.JWTService;
import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserModelDetailsService userModelDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromHeader(request);
        if (token == null || !token.startsWith("Bearer"))
        {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            token = clearTypeToken(token);
            DecodedJWT decodedJWT = JWTService.isValid(token);
            SecurityContextHolder.getContext().setAuthentication(getUsernamePasswordAuthenticationToken(decodedJWT));
            response.setStatus(HttpServletResponse.SC_GONE);
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException jwtVerificationException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jwtVerificationException.printStackTrace();
        }
    }

    /**
     * @param request Requisição
     * @return Token existente na requisição
     */
    private String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }
        return token;
    }

    /**
     * Entrada: Bearer: 12345
     * Saída: 12345
     *
     * @param token Token vindo da requisição
     * @return 'String' Recupera somente o token vindo na requisição.
     */
    private String clearTypeToken(String token) {
        return token.substring(7);
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(DecodedJWT decodedJWT)
    {
        UserDetails userDetails = userModelDetailsService.loadUserByUsername(decodedJWT.getClaim("USERNAME").asString());
        if (userDetails == null) {
            throw new BadCredentialsException("O usuário não foi localizado");
        }
        return createV2(userDetails);
    }

    private UsernamePasswordAuthenticationToken createV2(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(userDetails);
        return usernamePasswordAuthenticationToken;
    }
}
