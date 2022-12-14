package edu.utica.slce.jobsub.config.database;

import com.sct.messaging.bif.BatchResourceHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.Connection;

@Configuration
public class DataSourceConfig {

    @Bean
    SingleConnectionDataSource getDataSource() {
        return new SingleConnectionDataSource(BatchResourceHolder.getConnection(),true);
    }

    @Bean
    Connection getConnection() {
        return BatchResourceHolder.getConnection();
    }

}
