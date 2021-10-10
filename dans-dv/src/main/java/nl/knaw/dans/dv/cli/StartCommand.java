package nl.knaw.dans.dv.cli;

import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import nl.knaw.dans.dv.DansDvConfiguration;

public class StartCommand extends ConfiguredCommand<DansDvConfiguration> {

    public StartCommand() {
        super("start", "connect to the configured Dataverse instance");
    }

    @Override
    public void configure(Subparser subparser) {
        super.configure(subparser);
        Subparser dataverse = subparser.addSubparsers().addParser("dataverse");

    }

    @Override
    protected void run(Bootstrap<DansDvConfiguration> bootstrap, Namespace namespace, DansDvConfiguration configuration) throws Exception {

    }
}
