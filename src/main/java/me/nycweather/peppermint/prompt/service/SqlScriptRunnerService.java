package me.nycweather.peppermint.prompt.service;

import lombok.extern.slf4j.Slf4j;
import me.nycweather.peppermint.prompt.exception.SqlScriptExecutionException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SqlScriptRunnerService {

    private final JdbcTemplate jdbcTemplate;

    public SqlScriptRunnerService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void runScript(String scriptPath) throws SqlScriptExecutionException {
        log.info("Running script: {}", scriptPath);
        try {
            ClassPathResource resource = new ClassPathResource(scriptPath);
            String sql = new BufferedReader(new InputStreamReader(resource.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));
            jdbcTemplate.execute(sql);
            log.info("Script ran successfully: {}", scriptPath);
        } catch (Exception e) {
            log.error("Error running script: {}", scriptPath, e);
            throw new SqlScriptExecutionException("Error running script: " + scriptPath, e);
        }
    }
}
