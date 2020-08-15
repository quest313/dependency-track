/*
 * This file is part of Dependency-Track.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.dependencytrack.upgrade;

import alpine.Config;
import alpine.logging.Logger;
import alpine.persistence.JdoProperties;
import alpine.upgrade.UpgradeExecutor;
import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import org.dependencytrack.persistence.QueryManager;
import org.flywaydb.core.Flyway;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class UpgradeInitializer implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(UpgradeInitializer.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextInitialized(final ServletContextEvent event) {
        LOGGER.info("Initializing upgrade framework");

        final String driverPath = Config.getInstance().getProperty(Config.AlpineKey.DATABASE_DRIVER_PATH);
        if (driverPath != null) {
            Config.getInstance().expandClasspath(driverPath);
        }

        final String dbUrl = Config.getInstance().getProperty(Config.AlpineKey.DATABASE_URL);
        final String user = Config.getInstance().getProperty(Config.AlpineKey.DATABASE_USERNAME);
        final String password = Config.getInstance().getProperty(Config.AlpineKey.DATABASE_PASSWORD);
        if (dbUrl != null) {
            Config.getInstance().expandClasspath(dbUrl);
        }

        // Baseline Flyway from Alpine version
        baseline(dbUrl, user, password);

        Flyway flyway = Flyway.configure().locations("db/migration/postgres")
                .dataSource(dbUrl, user, password).load();

        flyway.migrate();
    }

    private void baseline(String dbUrl, String user, String password) {
        final JDOPersistenceManagerFactory pmf = (JDOPersistenceManagerFactory) JDOHelper.getPersistenceManagerFactory(JdoProperties.get(), "Alpine");
        try (final PersistenceManager pm = pmf.getPersistenceManager();
             final QueryManager qm = new QueryManager(pm)) {
            final UpgradeExecutor executor = new UpgradeExecutor(qm);
            String schemaVersion = executor.getSchemaVersion();

            if (schemaVersion == null) {
                // no baseline needed
                return;
            }

            LOGGER.info("Detected Alpine version: " + schemaVersion);
            LOGGER.info("Baseline flyway to version: " + schemaVersion);
            Flyway flyway = Flyway.configure().locations("db/migration/postgres")
                    .baselineVersion(schemaVersion)
                    .dataSource(dbUrl, user, password).load();

            flyway.baseline();
        }

        pmf.close();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        /* Intentionally blank to satisfy interface */
    }

}