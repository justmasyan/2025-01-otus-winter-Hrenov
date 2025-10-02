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
public class V040__Insert_groups extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        DataSource ds = new SingleConnectionDataSource(context.getConnection(), true);
        NamedParameterJdbcOperations jdbc = new NamedParameterJdbcTemplate(ds);
        insertGroups(jdbc);
        insertRelationsToStudents(jdbc);
    }

    private void insertGroups(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>(5);
        MigrationDataStorage.getSTUDY_GROUPS().forEach(groupDto -> params.add(Map.of(
                "code", groupDto.getCode(),
                "startDate", groupDto.getStartDate(),
                "courseId", groupDto.getCourse().getId()
        )));

        jdbc.batchUpdate("insert into study_groups (code, start_date, course_id)" +
                " values (:code, :startDate, :courseId)", params.toArray(new Map[0]));
    }

    private void insertRelationsToStudents(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>();
        MigrationDataStorage.getSTUDY_GROUPS().forEach(groupDto ->
                groupDto.getStudents().forEach(studentDto -> params.add(Map.of(
                        "studentId", studentDto.getId(),
                        "groupId", groupDto.getId())
                ))
        );

        jdbc.batchUpdate("insert into groups_students(group_id, student_id) " +
                "values (:groupId, :studentId)", params.toArray(new Map[0]));
    }

}
