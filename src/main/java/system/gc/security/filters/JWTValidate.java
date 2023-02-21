package system.gc.security.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import system.gc.security.UserModelDetailsService;
import system.gc.security.jwt.JWTService;
import java.io.IOException;
import java.util.List;

@Log4j2
public class JWTValidate extends OncePerRequestFilter {
    private final UserModelDetailsService userModelDetailsService;

    public JWTValidate(UserModelDetailsService userModelDetailsService) {
        this.userModelDetailsService = userModelDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("VALIDATE");
        List<String> permitted = List.of("/login");
        if (permitted.contains(request.getRequestURI()))
        {
            filterChain.doFilter(request, response);
            return;
        }
        String token = getTokenFromHeader(request);
        if (token == null || !token.startsWith("Bearer"))
        {
            log.warn("Requisição sem Bearer token");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            token = clearTypeToken(token);
            DecodedJWT decodedJWT = JWTService.isValid(token);
            SecurityContextHolder.getContext().setAuthentication(getUsernamePasswordAuthenticationToken(decodedJWT));
            log.info("Token de acesso valido");
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException jwtVerificationException) {
            jwtVerificationException.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
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
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(userDetails);
        return usernamePasswordAuthenticationToken;
    }

}
