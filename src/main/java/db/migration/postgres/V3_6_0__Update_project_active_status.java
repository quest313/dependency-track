package db.migration.postgres;


import alpine.util.DbUtil;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Connection;

public class V3_6_0__Update_project_active_status extends BaseJavaMigration {

    private static final String STMT_1 = "UPDATE \"PROJECT\" SET \"ACTIVE\" = TRUE WHERE \"ACTIVE\" IS NULL";
    private static final String STMT_1_ALT = "UPDATE \"PROJECT\" SET \"ACTIVE\" = 1 WHERE \"ACTIVE\" IS NULL";

    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        try {
            DbUtil.executeUpdate(connection, STMT_1);
        } catch (Exception e) {
            // Internal field is likely not boolean. Attempting component internal status update assuming bit field
            DbUtil.executeUpdate(connection, STMT_1_ALT);
        }
    }
}