import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.*;

/**
 * @author WuYujie
 * @email coffee377@dingtalk.com
 * @since 0.0.1
 */
@Slf4j
public class PasswordEncoderTest {
    String password = "password";
    @Test
    void addClient() {
        String plain = "password";
        StandardPasswordEncoder standardPasswordEncoder = new StandardPasswordEncoder();
        String encode1 = standardPasswordEncoder.encode(plain);
        MessageDigestPasswordEncoder messageDigestPasswordEncoder = new MessageDigestPasswordEncoder("MD5");
        String encode2 = messageDigestPasswordEncoder.encode(plain);
        log.warn("{} => {}", encode1, encode2);
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder("");
        pbkdf2PasswordEncoder.setAlgorithm(Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512);
        String encode = pbkdf2PasswordEncoder.encode("password");
        log.info("{}", encode);
    }

    @Test
    void MessageDigest(){
        PasswordEncoder encoder = new StandardPasswordEncoder();
        String encode = encoder.encode(password);
        log.info("{}", encode);

//        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        boolean matches = passwordEncoder.matches(password, "{MD5}{p4WvEEfqFKAOzk8KAzWjFHHy11P75mkl0PoysOA0hzQ=}2f540796b44499cf8a1c509a22bdb2b3");
    }
}
