import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class UserTest {

    @Test
    public void user() {
        User user = new User("demo", 35);
        log.info("{} => {}", user.getName(), user.getAge());
    }
}