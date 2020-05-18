package com.mearud;

import com.mearud.util.ColorUtility;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import com.mearud.cmdparser.CMDParser;
import com.mearud.util.ProjectUtility;
import com.mearud.util.StringUtility;

import java.net.PortUnreachableException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        CMDParser cmd = new CMDParser(args);
        List<String> ins = cmd.getCommandInstructionSet();
        String command = ins.remove(0);
        Function<List<String>, Boolean> mearudFunction = ProjectUtility.projectMap.get(command);
        if (mearudFunction == null) {
            //there is no know instruction
            System.out.println(ColorUtility.wrapError("No known mearud function "+command+"\n"));
            cmd.printHelpStmt();
        }

        Function<List<String>, String> listAsString = (ls) -> {
            StringBuilder b = new StringBuilder();
            ls.forEach( (in) -> { b.append(in+" "); });
            b.append("\n");
            return b.toString();
        };

        if (mearudFunction.apply(ins)) {
            System.out.println(ColorUtility.wrapSuccess("Mearud faithfully executed ["+command+"] for {"+listAsString.apply(ins)+"}"));
        }
        else {
            System.out.println(ColorUtility.wrapError("Mearud failed to successfully execute ["+command+"] for {"+listAsString.apply(ins)+"}"));
        }
     }
}
