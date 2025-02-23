package ai.platformcode.diagram.config;


import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.io.IOException;

@TestConfiguration
public class EmbeddedPostgresConfig {

    @Bean
    @Primary
    public DataSource dataSource() throws IOException {
        return EmbeddedPostgres.builder()
                .setPort(5432) // Usa a porta 5432 para simular um PostgreSQL real
                .start()
                .getPostgresDatabase();
    }
}