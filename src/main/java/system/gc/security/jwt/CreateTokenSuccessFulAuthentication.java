package system.gc.security.jwt;

import jakarta.servlet.http.HttpServletResponse;
import system.gc.dtos.TokenDTO;
import system.gc.utils.TextUtils;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public interface CreateTokenSuccessFulAuthentication {
    default void createTokenSuccessFulAuthentication(HttpServletResponse response,
                                                     final Map<String, String> params) throws Exception {
        writeToken(JWTService.createTokenJWT(params, LocalDateTime.now(), 5), response);
    }

    private void writeToken(String token, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(TextUtils.GSON.toJson(TokenDTO.builder().type("Bearer").token(token).build()));
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}
