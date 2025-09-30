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
public class V035__Insert_students extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        DataSource ds = new SingleConnectionDataSource(context.getConnection(), true);
        NamedParameterJdbcOperations jdbc = new NamedParameterJdbcTemplate(ds);
        insertStudents(jdbc);
    }

    private void insertStudents(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>();
        data().getSTUDENTS().forEach(studentDto -> params.add(Map.of(
                "firstName", studentDto.getFirstName(),
                "lastName", studentDto.getLastName(),
                "studyPlace", studentDto.getStudyPlace(),
                "accountId", studentDto.getAccount().getId()
        )));

        jdbc.batchUpdate("insert into students (first_name, last_name, study_place, account_id) " +
                "values (:firstName, :lastName, :studyPlace, :accountId)", params.toArray(new Map[0]));
    }
}
