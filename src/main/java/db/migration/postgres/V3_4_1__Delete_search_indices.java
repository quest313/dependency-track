package db.migration.postgres;


import org.dependencytrack.search.IndexManager;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V3_4_1__Delete_search_indices extends BaseJavaMigration {

    public void migrate(Context context) throws Exception {
        IndexManager.delete(IndexManager.IndexType.LICENSE);
        IndexManager.delete(IndexManager.IndexType.PROJECT);
        IndexManager.delete(IndexManager.IndexType.COMPONENT);
        IndexManager.delete(IndexManager.IndexType.VULNERABILITY);
    }
}