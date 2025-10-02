package ru.otus.project.config;

import db.data.MigrationDataStorage;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(false)
                .baselineVersion("0")
                .validateOnMigrate(false)
                .outOfOrder(false)
                .load();
    }

    @Bean
    public MigrationDataStorage migrationDataStorage(PasswordEncoder passwordEncoder){
        return new MigrationDataStorage(passwordEncoder);
    }
}
