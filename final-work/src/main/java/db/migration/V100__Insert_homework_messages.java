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
public class V100__Insert_homework_messages extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        DataSource ds = new SingleConnectionDataSource(context.getConnection(), true);
        NamedParameterJdbcOperations jdbc = new NamedParameterJdbcTemplate(ds);
        insertHomeworkMessages(jdbc);
    }

    private void insertHomeworkMessages(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>();
        MigrationDataStorage.getHOMEWORK_MESSAGES().forEach(messageDto -> params.add(Map.of(
                "text", messageDto.getText(),
                "sendTime", messageDto.getSendTime(),
                "homeworkId", messageDto.getHomework().getId(),
                "senderDesc", messageDto.getSenderDesc(),
                "senderId", messageDto.getSender().getId()
        )));

        jdbc.batchUpdate("insert into messages (text, send_time, homework_id, sender_desc, sender_id)" +
                " values (:text, :sendTime, :homeworkId, :senderDesc, :senderId)", params.toArray(new Map[0]));
    }
}
