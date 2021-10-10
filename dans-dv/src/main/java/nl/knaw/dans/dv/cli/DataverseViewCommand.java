package nl.knaw.dans.dv.cli;

import io.dropwizard.cli.Command;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public class DataverseViewCommand extends Command {

    public DataverseViewCommand() {
        super("view", "display metadata about a dataverse");
    }

    @Override
    public void configure(Subparser subparser) {

    }

    @Override
    public void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {

    }
}
