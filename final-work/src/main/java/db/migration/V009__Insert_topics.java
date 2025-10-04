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
public class V009__Insert_topics extends BaseJavaMigration {

    public static final Integer TOPICS_COUNT = 30;

    @Override
    public void migrate(Context context) throws Exception {
        DataSource ds = new SingleConnectionDataSource(context.getConnection(), true);
        NamedParameterJdbcOperations jdbc = new NamedParameterJdbcTemplate(ds);
        insertTopics(jdbc);
    }

    private void insertTopics(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>(5);
        MigrationDataStorage.getLECTURE_TOPICS().forEach(topic -> params.add(Map.of(
                "title", topic.getTitle())
        ));

        jdbc.batchUpdate("insert into lecture_topics(title) values (:title)", params.toArray(new Map[0]));
    }
}
