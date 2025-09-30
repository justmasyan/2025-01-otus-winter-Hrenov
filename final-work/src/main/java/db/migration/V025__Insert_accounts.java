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
public class V025__Insert_accounts extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        DataSource ds = new SingleConnectionDataSource(context.getConnection(), true);
        NamedParameterJdbcOperations jdbc = new NamedParameterJdbcTemplate(ds);
        insertAccounts(jdbc);
    }

    private void insertAccounts(NamedParameterJdbcOperations jdbc) {
        List<Map<String, Object>> params = new ArrayList<>(5);
        data().getACCOUNTS().forEach(accountDto -> params.add(Map.of(
                "username", accountDto.getUsername(),
                "password", accountDto.getPassword(),
                "roleId", accountDto.getAuthority().getId()
        )));

        jdbc.batchUpdate("insert into accounts (username, password, role_id)" +
                " values (:username, :password, :roleId)", params.toArray(new Map[0]));
    }
}
