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
package nl.knaw.dans.dv.cli;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import nl.knaw.dans.dv.DansDvConfiguration;
import org.checkerframework.checker.units.qual.N;

public class DataverseCommand extends ConfiguredCommand<DansDvConfiguration> {

    public DataverseCommand(Application<DansDvConfiguration> application) {
        super("dataverse", "operations on a single dataverse collection");
    }

    @Override
    public void configure(Subparser subparser) {
//        super.configure(subparser);
        subparser.addArgument("alias");
        Subparsers p = subparser.addSubparsers();
        Subparser view = p.addParser("view");

    }

    @Override
    protected void run(Bootstrap<DansDvConfiguration> bootstrap, Namespace namespace, DansDvConfiguration configuration) throws Exception {
        System.out.println("Running Dataverse command");
    }

    @Override
    public void run(Bootstrap<?> wildcardBootstrap, Namespace namespace) throws Exception {
        super.run(wildcardBootstrap, namespace);
    }
}
