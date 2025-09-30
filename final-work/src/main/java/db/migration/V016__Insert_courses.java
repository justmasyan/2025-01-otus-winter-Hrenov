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
public class V016__Insert_courses extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        DataSource ds = new SingleConnectionDataSource(context.getConnection(), true);
        NamedParameterJdbcOperations jdbc = new NamedParameterJdbcTemplate(ds);
        insertCourses(jdbc);
        insertRelationsToTopics(jdbc);
    }

    private void insertCourses(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>();
        data().getCOURSES().forEach(courseDto -> params.add(Map.of(
                "title", courseDto.getTitle(),
                "price", courseDto.getPrice())
        ));

        jdbc.batchUpdate("insert into courses(title, price) values (:title, :price)", params.toArray(new Map[0]));
    }

    private void insertRelationsToTopics(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>();
        data().getCOURSES().forEach(courseDto ->
                courseDto.getLectureTopics().forEach(topicDto -> params.add(Map.of(
                        "courseId", courseDto.getId(),
                        "topicId", topicDto.getId())
                ))
        );

        jdbc.batchUpdate("insert into courses_topics(course_id, topic_id) " +
                "values (:courseId, :topicId)", params.toArray(new Map[0]));
    }
}

