package io.gary;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



class Framework {

    static final Logger logger = LogManager.getLogger(Framework.class);
    static Logger getLogger(){
        return logger;
    }
}

public class Main {
    public static void main(String[] args) {
        Options options = new Options();

        Option userOption = new Option("u", "user", true, "db user");
        userOption.setRequired(true);
        options.addOption(userOption);

        Option passOption = new Option("p", "password", true, "db password");
        passOption.setRequired(true);
        options.addOption(passOption);

        Option dbUrlOption = new Option( "h", "dburl", true, "db url");
        dbUrlOption.setRequired(true);
        options.addOption(dbUrlOption);

        Option searchPathOption = new Option( "d", "path", true, "db url");
        searchPathOption.setRequired(true);
        options.addOption(searchPathOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("load-data", options);
            System.exit(1);
            return;
        }

        String dbUser = cmd.getOptionValue("user");
        String dbPass = cmd.getOptionValue("password");
        String dbUrl = cmd.getOptionValue("dburl");
        String path = cmd.getOptionValue("path");


        Framework.getLogger().info("user {}, password {}, dburl {} ,path {}",dbUser,dbPass,dbUrl,path);
        LoadTask task = new LoadTask(dbUrl,dbUser,dbPass,path);
        task.Execute();

    }
}

