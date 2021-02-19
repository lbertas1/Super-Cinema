package bartos.lukasz.service.tokens;

import bartos.lukasz.dto.getModel.GetUser;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${tokens.access-token.expiration-time-ms}")
    private Long accessTokenExpirationTimeMs;

    @Value("${tokens.refresh-token.expiration-time-ms}")
    private Long refreshTokenExpirationTimeMs;

    @Value("${tokens.refresh-token.property}")
    private String refreshTokenProperty;

    @Value("${tokens.prefix}")
    private String tokensPrefix;

    private final SecretKey secretKey;
    private final UserRepository userRepository;

    public Tokens generateTokens(GetUser user) {
        Date currentDate = new Date();

//        long accessTokenExpirationTimeMs = 5 * 60 * 1000;
        Date accessTokenExpirationDate = new Date(currentDate.getTime() + accessTokenExpirationTimeMs);

//        long refreshTokenExpirationTimeMs = 100 * 5 * 60 * 1000;
        Date refreshTokenExpirationDate = new Date(currentDate.getTime() + accessTokenExpirationTimeMs);

        var accessToken = Jwts
                .builder()
                .setSubject(user.getUsername())
                .setExpiration(accessTokenExpirationDate)
                .setIssuedAt(currentDate)
                .signWith(secretKey)
                .compact();

        var refreshToken = Jwts
                .builder()
                .setSubject(user.getUsername())
                .setExpiration(refreshTokenExpirationDate)
                .setIssuedAt(currentDate)
                .signWith(secretKey)
                .claim(refreshTokenProperty, accessTokenExpirationDate.getTime())
                .compact();

        return Tokens
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public GetUser parseAccessToken(String header) {
        if (header == null) {
            throw new ServiceException("Header is null");
        }

        if (!header.startsWith(tokensPrefix)) {
            throw new ServiceException("Header has incorrect format");
        }

        var token = header.replaceAll(tokensPrefix, "");

        if (!isTokenValid(token)) {
            throw new ServiceException("Token is not valid");
        }

        var userId = id(token);
        return userRepository
                .findUserByUsername(userId)
                .map(GetModelMappers::toGetUser)
                .orElseThrow(() -> new ServiceException("Cannot find user with id " + userId));
    }

    private Claims claims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String id(String token) {
        return claims(token).getSubject();
    }

    private Date expirationDate(String token) {
        return claims(token).getExpiration();
    }

    private boolean isTokenValid(String token) {
        return expirationDate(token).after(new Date());
    }
}
