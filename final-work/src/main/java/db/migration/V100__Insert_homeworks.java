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
public class V100__Insert_homeworks extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        DataSource ds = new SingleConnectionDataSource(context.getConnection(), true);
        NamedParameterJdbcOperations jdbc = new NamedParameterJdbcTemplate(ds);
        insertHomeworks(jdbc);
        insertRelationsToMessages(jdbc);
    }

    private void insertHomeworks(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>();
        data().getHOMEWORKS().forEach(homeworkDto -> params.add(Map.of(
                "mark", homeworkDto.getMark(),
                "statusId", homeworkDto.getStatus().getId(),
                "lectureId", homeworkDto.getLecture().getId(),
                "lectorId", homeworkDto.getLector().getId(),
                "studentId", homeworkDto.getStudent().getId()
        )));

        jdbc.batchUpdate("insert into homeworks (mark, status_id, lecture_id, lector_id, student_id)" +
                " values (:mark, :statusId, :lectureId, :lectorId, :studentId)", params.toArray(new Map[0]));
    }

    private void insertRelationsToMessages(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>();
        data().getHOMEWORKS().forEach(homeworkDto -> homeworkDto.getMessages()
                .forEach(messageDto -> params.add(Map.of(
                        "homeworkId", homeworkDto.getId(),
                        "messageId", messageDto.getId()))
                )
        );

        jdbc.batchUpdate("insert into homeworks_messages (homework_id,message_id)" +
                " values (:homeworkId, :messageId)", params.toArray(new Map[0]));
    }
}
