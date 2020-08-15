package db.migration.postgres;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

public class V3_8_5__Assign_uuids_to_existing_repositories extends BaseJavaMigration {

    private static final String STMT_1 = "SELECT * FROM \"REPOSITORY\"";
    private static final String STMT_2 = "UPDATE \"REPOSITORY\" SET \"UUID\" = ? WHERE \"ID\" = ?";

    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        final Statement stmt = connection.createStatement();
        try {
            final ResultSet rs = stmt.executeQuery(STMT_1);
            while (rs.next()) {
                final PreparedStatement ps = connection.prepareStatement(STMT_2);
                ps.setString(1, UUID.randomUUID().toString());
                ps.setLong(2, rs.getLong(1));
                ps.executeUpdate();
            }
        } finally {
            stmt.close();
        }
    }
}