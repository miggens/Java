package com.mearud.cmdparser;

import com.mearud.util.ColorUtility;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.HelpFormatter;

import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;
import java.util.function.BiConsumer;


public class CMDParser {

    private CommandLineParser clp;
    private CommandLine cmd;
    private Options opts;
    private List<String> instructions;

    public CMDParser(String[] args) {
        this.instructions = new ArrayList<>();

        this.opts = new Options();

        opts.addOption("n", "new", true, "Command [new] is used to create new [A, B] projects.");
        // new <TYPE(FRONT|BACK)> <APP_NAME>
        opts.addOption("r", "run", true, "Command [run] is used to run a dev project | docker image assembled.");
        //run (prod|dev)
        opts.addOption("d", "deploy", true, "Command [deploy] is used to push final artifacts to a cloud or deploy location");
        opts.addOption( Option.builder().argName("h").longOpt("help").desc("Command [help] prints the formatted help information").build());

        //deploy <SERVICE> <LOCATION>
        this.clp = new DefaultParser();

        BiConsumer<String, String> printHelpStmt = (header, footer) -> {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("mearud <cmd> <>", header, opts, footer);
        };

        try  {

            this.cmd = clp.parse(opts, args);
            Option[] options = this.cmd.getOptions();
            //Arrays.stream( options ).forEach(option -> {System.out.println(option.toString());} );
            if (this.cmd.hasOption("help") || this.cmd.hasOption("h")) {
                //print help message and allow to complete... move functionality later

                String header = ColorUtility.wrapInfo2("\nMearud is a productivity tools striving to make lowly developer dreams come true...\n");
                String footer = "\nThank you for choosing Mearud!";
                printHelpStmt.accept(header, footer);
            }
            else if (this.cmd.hasOption("new") || this.cmd.hasOption("n")) {
                List<String> optVals = this.cmd.getArgList();
                String val = this.cmd.getOptionValue("new");
                this.instructions.add(val);
                optVals.forEach( (v) -> {this.instructions.add(v);});
                // validate the appropriate args are present
                // proceed to create new <type> <project_name>
                // evaluate for additional args and apply
            }
            else if (this.cmd.hasOption("run") || this.cmd.hasOption("r")) {
                // validate the appropriate args are present
                // proceed to (build|pull|read|???) docker <env>
                // construct and run docker image
                // validate for success and report stay open for some type of orchestration.
            }
            else if (this.cmd.hasOption("deploy") || this.cmd.hasOption("d")) {
                // validate the appropriate args are present
                // proceed to read/eval <cfg> | default to running a docker image
                // <cfg> types: AWS, Google Cloud, Digital Ocean to begin.
                // validate success and report.
            }
            else {
                //invalid options... report error and complete.

            }
        }
        catch (ParseException pe) {
            System.out.println("[ParseException] -> "+pe.getMessage());
        }

    }

    public List<String> getCommandInstructionSet() {
        return this.instructions;
    }

    public void printHelpStmt() {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("mearud <cmd> <>", this.opts);
    }

}
