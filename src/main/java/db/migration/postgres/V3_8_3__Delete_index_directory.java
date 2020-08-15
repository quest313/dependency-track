package db.migration.postgres;


import alpine.Config;
import org.apache.commons.io.FileDeleteStrategy;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.io.File;

public class V3_8_3__Delete_index_directory extends BaseJavaMigration {

    public void migrate(Context context) throws Exception {
        final String INDEX_ROOT_DIR = Config.getInstance().getDataDirectorty().getAbsolutePath() + File.separator + "index";
        FileDeleteStrategy.FORCE.delete(new File(INDEX_ROOT_DIR));
    }
}