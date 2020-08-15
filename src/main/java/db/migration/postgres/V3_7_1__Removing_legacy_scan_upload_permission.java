package db.migration.postgres;


import alpine.util.DbUtil;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class V3_7_1__Removing_legacy_scan_upload_permission extends BaseJavaMigration {


    private static final String STMT = "SELECT \"ID\" FROM \"PERMISSION\" WHERE \"NAME\" = 'SCAN_UPLOAD'";


    private static final String STMT_1 = "DELETE FROM \"TEAMS_PERMISSIONS\" WHERE \"PERMISSION_ID\" = %d";
    private static final String STMT_2 = "DELETE FROM \"LDAPUSERS_PERMISSIONS\" WHERE \"PERMISSION_ID\" = %d";
    private static final String STMT_3 = "DELETE FROM \"MANAGEDUSERS_PERMISSIONS\" WHERE \"PERMISSION_ID\" = %d";
    private static final String STMT_4 = "DELETE FROM \"PERMISSION\" WHERE \"ID\" = %d";

    public void migrate(Context context) throws Exception {

        Connection connection = context.getConnection();
        final Statement q = connection.createStatement();
        final ResultSet rs = q.executeQuery(STMT);
        while(rs.next()) {
            final long id = rs.getLong(1);
           // Removing SCAN_UPLOAD from the TEAMS_PERMISSIONS table
            DbUtil.executeUpdate(connection, String.format(STMT_1, id));
            // Removing SCAN_UPLOAD from the LDAPUSERS_PERMISSIONS table
            DbUtil.executeUpdate(connection, String.format(STMT_2, id));
            // Removing SCAN_UPLOAD from the MANAGEDUSERS_PERMISSIONS table
            DbUtil.executeUpdate(connection, String.format(STMT_3, id));
            // Removing SCAN_UPLOAD from the PERMISSION table
            DbUtil.executeUpdate(connection, String.format(STMT_4, id));
        }
    }
}