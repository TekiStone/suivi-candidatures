package fr.conesa.candidatures.config;

import java.time.Clock;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class AppConfig {

    /** Horloge injectée partout : permet de figer la date dans les tests. */
    @Bean
    public Clock horloge() {
        return Clock.system(ZoneId.of("Europe/Paris"));
    }
}
