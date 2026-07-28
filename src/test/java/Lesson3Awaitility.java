import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

public class Lesson3Awaitility {
    @Test
    void isNowIsEvenMilli(){
        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> {assert isTimestampEven();});
        }

        private boolean isTimestampEven(){
        return Instant.now().toEpochMilli()%2 == 0;
        }
}
