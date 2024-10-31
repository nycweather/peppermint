package me.nycweather.peppermint.prompt.component;

import lombok.extern.slf4j.Slf4j;
import me.nycweather.peppermint.prompt.exception.SqlScriptExecutionException;
import me.nycweather.peppermint.prompt.service.SqlScriptRunnerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SqlScriptRunner implements CommandLineRunner {

    private final SqlScriptRunnerService sqlScriptRunnerService;

    public SqlScriptRunner(SqlScriptRunnerService sqlScriptRunnerService) {
        this.sqlScriptRunnerService = sqlScriptRunnerService;
    }

    @Override
    public void run(String[] args) {
        try {
            log.info("Running sql scripts");
            sqlScriptRunnerService.runScript("sql/extensions.sql");
        } catch (SqlScriptExecutionException e) {
            log.error("Error running sql scripts", e);
        }
    }

}
