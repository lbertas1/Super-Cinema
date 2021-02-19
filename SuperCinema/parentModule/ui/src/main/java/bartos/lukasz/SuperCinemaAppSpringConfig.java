package bartos.lukasz;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

@Configuration
@ComponentScan("bartos.lukasz")
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class SuperCinemaAppSpringConfig {

    private final Environment environment;

    @Bean
    public SecretKey secretKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public Jdbi jdbi() throws ClassNotFoundException {
        var username = environment.getRequiredProperty("spring.database.username");
        var password = environment.getRequiredProperty("spring.database.password");
        var url = environment.getRequiredProperty("spring.database.url");
        return Jdbi.create(url, username, password);
    }

//    @Bean
//    public Jdbi jdbi() throws ClassNotFoundException {
//        var username = environment.getRequiredProperty("database.username");
//        var password = environment.getRequiredProperty("database.password");
//        var url = environment.getRequiredProperty("database.url");
//        return Jdbi.create(url, username, password);
//    }
}
