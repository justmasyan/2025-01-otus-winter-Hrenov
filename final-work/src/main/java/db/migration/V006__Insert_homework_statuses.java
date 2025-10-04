package db.migration;

import db.data.MigrationDataStorage;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class V006__Insert_homework_statuses extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        DataSource ds = new SingleConnectionDataSource(context.getConnection(), true);
        NamedParameterJdbcOperations jdbc = new NamedParameterJdbcTemplate(ds);
        insertHomeworkStatuses(jdbc);
    }

    private void insertHomeworkStatuses(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>(2);
        MigrationDataStorage.getHOMEWORK_STATUSES().forEach(statusDto -> params.add(Map.of(
                "text", statusDto.getText())
        ));
        jdbc.batchUpdate("insert into homework_statuses(text) values (:text)", params.toArray(new Map[0]));
    }
}
