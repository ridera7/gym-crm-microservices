package com.integration.test.service.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component
public class PostgreSqlRepository {

    private JdbcTemplate jdbcTemplate;

    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.datasource.url}")
    private String databaseUrl;
    @Value("${spring.datasource.username}")
    private String databaseUser;
    @Value("${spring.datasource.password}")
    private String databasePassword;

    @PostConstruct
    private void initialize() {
        DataSource dataSource = new DriverManagerDataSource() {
            {
                setDriverClassName(driver);
                setUrl(databaseUrl);
                setUsername(databaseUser);
                setPassword(databasePassword);
            }
        };
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int insert(String sql, Object... params) {
        return jdbcTemplate.update(sql, params);
    }

    public int updateOrDelete(String sql, Object... params) {
        return jdbcTemplate.update(sql, params);
    }

    public <T> T queryForObject(String sql, Class<T> requiredType, Object... params) {
        return jdbcTemplate.queryForObject(sql, requiredType, params);
    }

    public <T> List<T> queryForList(String sql, Class<T> elementType, Object... params) {
        return jdbcTemplate.queryForList(sql, elementType, params);
    }

    public List<Map<String, Object>> queryForAllRows(String sql, Object... params) {
        return jdbcTemplate.queryForList(sql, params);
    }

    public void clearTables(String... tableNames) {
        for (String tableName : tableNames) {
            String sql = "DELETE FROM public." + tableName;  // Удаляет все данные из таблицы
            jdbcTemplate.update(sql);
        }
    }

    public boolean isConnected() {
        try {
            jdbcTemplate.execute("SELECT 1");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

