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
public class V055__Insert_lecture_materials extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        DataSource ds = new SingleConnectionDataSource(context.getConnection(), true);
        NamedParameterJdbcOperations jdbc = new NamedParameterJdbcTemplate(ds);
        insertLectureMaterials(jdbc);
    }

    private void insertLectureMaterials(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>();
        MigrationDataStorage.getLECTURE_MATERIALS().forEach(materialDto -> params.add(Map.of(
                "url", materialDto.getUrl(),
                "lectureId", materialDto.getLecture().getId()
        )));

        jdbc.batchUpdate("insert into lecture_materials (url, lecture_id)" +
                " values (:url, :lectureId)", params.toArray(new Map[0]));
    }
}
