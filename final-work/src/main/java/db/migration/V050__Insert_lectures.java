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
public class V050__Insert_lectures extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        DataSource ds = new SingleConnectionDataSource(context.getConnection(), true);
        NamedParameterJdbcOperations jdbc = new NamedParameterJdbcTemplate(ds);
        insertLectures(jdbc);
        insertRelationsToMaterials(jdbc);
    }

    private void insertLectures(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>();
        data().getLECTURES().forEach(lectureDto -> params.add(Map.of(
                "lectureTime", lectureDto.getLectureTime(),
                "status_id", lectureDto.getStatus().getId(),
                "topicId", lectureDto.getLectureTopic().getId(),
                "lectorId", lectureDto.getLector().getId(),
                "groupId", lectureDto.getStudyGroup().getId()
        )));

        jdbc.batchUpdate("insert into lectures (lecture_time, status_id, topic_id, lector_id,group_id)" +
                " values (:lectureTime, :status_id, :topicId, :lectorId, :groupId)", params.toArray(new Map[0]));
    }

    private void insertRelationsToMaterials(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>();
        data().getLECTURES().forEach(lectureDto ->
                lectureDto.getMaterials().forEach(materialDto -> params.add(Map.of(
                        "lectureId", lectureDto.getId(),
                        "materialId", materialDto.getId())
                ))
        );

        jdbc.batchUpdate("insert into lectures_materials(lecture_id, material_id) " +
                "values (:lectureId, :materialId)", params.toArray(new Map[0]));
    }
}
