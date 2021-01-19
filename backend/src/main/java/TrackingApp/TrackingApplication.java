package TrackingApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
public class TrackingApplication {
    public static void main(final String[] args) {
        SpringApplication.run(TrackingApplication.class, args);
    }
}

@Configuration
@EnableScheduling
class SchedulingConfiguration {

}
