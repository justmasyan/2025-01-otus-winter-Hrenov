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
public class V030__Insert_lectors extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        DataSource ds = new SingleConnectionDataSource(context.getConnection(), true);
        NamedParameterJdbcOperations jdbc = new NamedParameterJdbcTemplate(ds);
        insertStudents(jdbc);
        insertRelationsToCourses(jdbc);
    }

    private void insertStudents(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>();
        MigrationDataStorage.getLECTORS().forEach(lectorDto -> params.add(Map.of(
                "firstName", lectorDto.getFirstName(),
                "lastName", lectorDto.getLastName(),
                "jobTitle", lectorDto.getJobTitle(),
                "accountId", lectorDto.getAccount().getId()
        )));

        jdbc.batchUpdate("insert into lectors (first_name, last_name, job_title, account_id) " +
                "values (:firstName, :lastName, :jobTitle, :accountId)", params.toArray(new Map[0]));
    }

    private void insertRelationsToCourses(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>();
        MigrationDataStorage.getLECTORS().forEach(lectorDto ->
                lectorDto.getCourses().forEach(courseDto -> params.add(Map.of(
                        "courseId", courseDto.getId(),
                        "lectorId", lectorDto.getId())
                ))
        );

        jdbc.batchUpdate("insert into courses_lectors(course_id, lector_id) " +
                "values (:courseId, :lectorId)", params.toArray(new Map[0]));
    }
}
