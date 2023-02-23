package system.gc.security.jwt;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

public class JWTService {
    @Autowired
    static Environment environment;
    /**
     * @param claims Parametros para compor a chave autenticada
     * @param TIME_TOKEN        Validade do token
     * @return 'String'         Token criado
     */
    public static String createTokenJWT(final Map<String, String> claims, Long TIME_TOKEN) {
        JWTCreator.Builder builder = JWT.create()
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TIME_TOKEN));
        claims.forEach(builder::withClaim);
        return builder.sign(Algorithm.HMAC256(System.getenv("PRIVATE_KEY_TOKEN")));
    }

    /**
     * @param claims Parametros para compor a chave autenticada
     * @param localDateTimeNow  Data e hora inicial
     * @param TIME        Validade do token. Quantidade de horas para o token expirar
     * @return 'String'         Token criado
     */
    public static String createTokenJWT(final Map<String, String> claims, LocalDateTime localDateTimeNow, final int TIME) {
        JWTCreator.Builder builder = JWT.create()
                .withIssuedAt(localDateTimeNow.atZone(ZoneId.systemDefault()).toInstant())
                .withExpiresAt(localDateTimeNow.plusMinutes(TIME).atZone(ZoneId.systemDefault()).toInstant());
        claims.forEach(builder::withClaim);
        return builder.sign(Algorithm.HMAC256(System.getenv("PRIVATE_KEY_TOKEN")));
    }

    public static DecodedJWT isValid(String token) throws JWTVerificationException {
        return JWT
                .require(Algorithm.HMAC256(System.getenv("PRIVATE_KEY_TOKEN")))
                .build()
                .verify(token);
    }
}
