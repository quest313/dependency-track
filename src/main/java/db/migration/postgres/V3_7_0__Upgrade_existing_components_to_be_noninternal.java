package db.migration.postgres;


import alpine.util.DbUtil;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Connection;

public class V3_7_0__Upgrade_existing_components_to_be_noninternal extends BaseJavaMigration {


    private static final String STMT_1 = "UPDATE \"COMPONENT\" SET \"INTERNAL\" = FALSE WHERE \"INTERNAL\" IS NULL";
    private static final String STMT_1_ALT = "UPDATE \"COMPONENT\" SET \"INTERNAL\" = 0 WHERE \"INTERNAL\" IS NULL";

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