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
public class V014__Insert_roles extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        DataSource ds = new SingleConnectionDataSource(context.getConnection(), true);
        NamedParameterJdbcOperations jdbc = new NamedParameterJdbcTemplate(ds);
        insertRoles(jdbc);
    }

    private void insertRoles(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>(2);
        MigrationDataStorage.getACCOUNT_ROLES().forEach(role -> params.add(Map.of(
                "description", role.getDescription(), "authority", role.getAuthority())
        ));
        jdbc.batchUpdate("insert into roles(description, authority) " +
                "values (:description, :authority)", params.toArray(new Map[0]));
    }
}
