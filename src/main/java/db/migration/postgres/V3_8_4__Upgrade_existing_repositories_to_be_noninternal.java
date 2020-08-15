package db.migration.postgres;


import alpine.util.DbUtil;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Connection;

public class V3_8_4__Upgrade_existing_repositories_to_be_noninternal extends BaseJavaMigration {

    private static final String STMT = "UPDATE \"REPOSITORY\" SET \"INTERNAL\" = FALSE";
    private static final String STMT_ALT = "UPDATE \"REPOSITORY\" SET \"INTERNAL\" = 0";

    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        try {
            DbUtil.executeUpdate(connection, STMT);
        } catch (Exception e) {
            // Internal field is likely not boolean. Attempting repository internal status update assuming bit field
            DbUtil.executeUpdate(connection, STMT_ALT);
        }
    }
}