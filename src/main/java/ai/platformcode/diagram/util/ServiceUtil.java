package ai.platformcode.diagram.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

@Service
public class ServiceUtil {
	
    // Utility method to get current timestamp
    public ZonedDateTime getCurrentLocalDateTime() {
        //return LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        return ZonedDateTime.now(ZoneId.systemDefault());
    }

}
