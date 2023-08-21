package quan.dodomilktea.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import quan.dodomilktea.config.security.CustomAccountDetail;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@CrossOrigin(origins = "http://localhost:3000")
public class JwtTokenUlti {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUlti.class);
    private static final long EXPIRE_DURATION = 1*60*60*1000;//1 hour
    @Value("${app.jwt.secret}")
    private String secrectKey;

    public String generateAccessToken(CustomAccountDetail account){
        return Jwts.builder()
                .setSubject(account.getId() +","+account.getEmail()
                +","+account.getRole() +","+account.getName())
                .setIssuer("TraSuaDoDo")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS256, secrectKey)
                .compact();
    }

    public boolean validateAccessToken(String token){
        try {
            Jwts.parser().setSigningKey(secrectKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e){
            LOGGER.error("jwt expired:" , e);
        } catch (IllegalArgumentException e){
            LOGGER.error("token is null, empty or has only whitespacr", e);
        } catch (MalformedJwtException e){
            LOGGER.error("jwt invalid", e);
        } catch (UnsupportedJwtException e){
            LOGGER.error("jwt is not supported", e);
        } catch (SignatureException e){
            LOGGER.error("signature validation failed", e);
        }
        return false;
    }

    public String getSubject(String token){
        return parseClaims(token).getSubject();
    }
    private Claims parseClaims(String token){
        return Jwts.parser()
                .setSigningKey(secrectKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean hasAuthorizationHeader (HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(ObjectUtils.isEmpty(header) || !header.startsWith("BEARER")){
            return false;
        }
        return true;
    }

    public String getAccessToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String token = header.split(" ")[1].trim();
        return token;
    }
}
