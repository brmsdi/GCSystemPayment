package system.gc.security.filters;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import system.gc.dtos.ErrorDTO;
import system.gc.dtos.ErrorInfoDTO;
import system.gc.utils.TextUtils;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        authException.printStackTrace();
        ErrorInfoDTO errorInfoDTO = new ErrorInfoDTO(new ErrorDTO(HttpServletResponse.SC_UNAUTHORIZED, "Acesso negado"),
                List.of("Verifique todas as especificações",
                        "1. Realize login novamente para gerar o Bearer token",
                        "2. Bearer token deve estar presenta nas requisições",
                        "3. O Bearer token deve ser valido"));
        response
                .getWriter()
                .print(TextUtils.GSON.toJson(errorInfoDTO));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
