package db.migration.postgres;


import alpine.Config;
import org.apache.commons.io.FileDeleteStrategy;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.io.File;

public class V3_8_2__Delete_nist_directory extends BaseJavaMigration {

    public void migrate(Context context) throws Exception {
        final String NIST_ROOT_DIR = Config.getInstance().getDataDirectorty().getAbsolutePath() + File.separator + "nist";
        FileDeleteStrategy.FORCE.delete(new File(NIST_ROOT_DIR));
    }
}