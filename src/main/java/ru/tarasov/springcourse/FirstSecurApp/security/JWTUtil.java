package ru.tarasov.springcourse.FirstSecurApp.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;

    //генерируем токен, действителен 60 минут от момента создания
    public String generateToken(String username){
        Date experationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject("User Details") //что мы храним в токене
                .withClaim("username", username) // пары ключ-значение их может быть любое количество
                .withIssuedAt(new Date())// время когда токен создан
                .withIssuer("tarasov") // кто выдал токен, обвчно пишется имя нашего приложения
                .withExpiresAt(experationDate) // когда токен протухнет
                .sign(Algorithm.HMAC256(secret)) // подписываем токен и передаем секрет
                ;
    }

    //клиент посылает нам токен, мы его принимаем и валидируем
    //Валидируются только токены с одинаковым subject and issuer
     public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier jwtVerifier =  JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("tarasov")
                .build();
        // метод verify предоставляет нам декодированный jwt
        DecodedJWT jwt = jwtVerifier.verify(token);
        return jwt.getClaim("username").asString();

     }
}
