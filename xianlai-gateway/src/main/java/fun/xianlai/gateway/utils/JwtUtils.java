package fun.xianlai.gateway.utils;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jwt.JWTClaimsSet;

import java.text.ParseException;

/**
 * @author WyattLau
 */
public class JwtUtils {
    /**
     * 解析 JWT 获取 payload 数据
     */
    public static JWTClaimsSet parseJwtPayload(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            return JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
        } catch (ParseException e) {
            throw new RuntimeException("JWT解析失败");
        }
    }
}
