package nl.knaw.dans.dv.cli;

import io.dropwizard.cli.Command;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import java.util.HashMap;
import java.util.Map;

public class DelegatingCommand extends Command  {
    private final Map<String, Command> subCommands = new HashMap<>();


    public void addSubCommand(Command subCommand) {
        subCommands.put(subCommand.getName(), subCommand);
        subCommand.c
    }


    @Override
    public void configure(Subparser subparser) {

    }

    @Override
    public void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {

    }
}
