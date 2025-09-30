package db.migration;


import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static db.data.MigrationData.data;

@SuppressWarnings("unchecked")
public class V008__Insert_lecture_materials extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        DataSource ds = new SingleConnectionDataSource(context.getConnection(), true);
        NamedParameterJdbcOperations jdbc = new NamedParameterJdbcTemplate(ds);
        insertHomeworkStatuses(jdbc);
    }

    private void insertHomeworkStatuses(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>();
        data().getLECTURE_MATERIALS().forEach(materialsDto -> params.add(Map.of(
                "url", materialsDto.getUrl())
        ));
        jdbc.batchUpdate("insert into lecture_materials(url) values (:url)", params.toArray(new Map[0]));
    }
}
