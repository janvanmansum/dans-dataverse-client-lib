/*
 * Copyright (C) 2021 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.knaw.dans.dv;

import io.dropwizard.Application;
import io.dropwizard.cli.CheckCommand;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import nl.knaw.dans.dv.cli.DataverseCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DansDvApplication extends Application<DansDvConfiguration> {

    private static final Logger log = LoggerFactory.getLogger(DansDvApplication.class);

    public static void main(final String[] args) throws Exception {
        new DansDvApplication().run(args);
    }

    @Override
    public String getName() {
        return "DANS Dataverse CLI";
    }

    @Override
    protected void addDefaultCommands(Bootstrap<DansDvConfiguration> bootstrap) {
        log.info("Overridden addDefaultCommands");
        // Prevent server-command from being added
        bootstrap.addCommand(new CheckCommand<>(this));
    }

    @Override
    public void initialize(final Bootstrap<DansDvConfiguration> bootstrap) {
        bootstrap.addCommand(new DataverseCommand(this));
    }

    @Override
    public void run(final DansDvConfiguration configuration, final Environment environment) {
        // Nothing to do, as this is a CLI app
    }

}
