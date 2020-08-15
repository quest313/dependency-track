package db.migration.postgres;


import alpine.Config;
import org.apache.commons.io.FileDeleteStrategy;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.io.File;

public class V3_6_1__Delete_work_directory extends BaseJavaMigration {

    public void migrate(Context context) throws Exception {
        final String DC_ROOT_DIR = Config.getInstance().getDataDirectorty().getAbsolutePath() + File.separator + "dependency-check";
        FileDeleteStrategy.FORCE.delete(new File(DC_ROOT_DIR));
    }
}