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
public class V096__Insert_homeworks extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        DataSource ds = new SingleConnectionDataSource(context.getConnection(), true);
        NamedParameterJdbcOperations jdbc = new NamedParameterJdbcTemplate(ds);
        insertHomeworks(jdbc);
    }

    private void insertHomeworks(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>();
        MigrationDataStorage.getHOMEWORKS().forEach(homeworkDto -> params.add(Map.of(
                "mark", homeworkDto.getMark(),
                "statusId", homeworkDto.getStatus().getId(),
                "lectureId", homeworkDto.getLecture().getId(),
                "lectorId", homeworkDto.getLector().getId(),
                "studentId", homeworkDto.getStudent().getId()
        )));

        jdbc.batchUpdate("insert into homeworks (mark, status_id, lecture_id, lector_id, student_id)" +
                " values (:mark, :statusId, :lectureId, :lectorId, :studentId)", params.toArray(new Map[0]));
    }
}
