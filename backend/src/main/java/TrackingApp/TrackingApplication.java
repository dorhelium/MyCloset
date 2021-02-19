package TrackingApp;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
public class TrackingApplication {
    public static void main(final String[] args) {
        BasicConfigurator.configure();
        SpringApplication.run(TrackingApplication.class, args);
    }
}

@Configuration
@EnableScheduling
class SchedulingConfiguration {

}
