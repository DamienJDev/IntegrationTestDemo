package damo.demo.test;

import damo.demo.test.tests.HealthCheckTests;
import damo.demo.test.tests.Result;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.parseArgs(args);

        //just show command line info
        if(containsArg(args, "-h") || containsArg(args, "-help")){
            return;
        }

        //Run tests:
        List<Result> results = new LinkedList<Result>();
        HealthCheckTests healthCheckTests = new HealthCheckTests();
        healthCheckTests.runHealthCheckTests(results);

        ResultWriter resWriter = new ResultWriter(results);
        try {
            resWriter.writeToConsole();
        } catch (Exception e){
            System.out.println("unexpected exception while writing test results");
            e.printStackTrace();
        }
    }


    public void parseArgs(String[] args){
        System.out.println("demo integration test suite");
        System.out.println();
        System.out.println("to configure an endpoint use:");
        System.out.println("    -endpoint=<endpoint url>");
        System.out.println("    e.g. -endpoint=http://localhost");
        System.out.println("    defaults to http://localhost");
        System.out.println();
        System.out.println("to run only one specific test instead of all tests:");
        System.out.println("    -runtest=<test name>");
        System.out.println("    e.g. -runtest=testDemoPing");
        System.out.println();
        System.out.println("for verbose results use:");
        System.out.println("    -v");
        System.out.println();
        System.out.println();

        if(containsArg(args, "-v")){
            Config.getConfig().setVerboseResults(true);
        }
        if(containsArg(args, "-endpoint=")){
            Config.getConfig().setUrlEndpoint(getArgValue(args, "-endpoint"));
            if(Config.getConfig().getUrlEndpoint().endsWith("/")){
                Config.getConfig().setUrlEndpoint(Config.getConfig().getUrlEndpoint().substring(0, Config.getConfig().getUrlEndpoint().length()-1));
            } else if(Config.getConfig().getUrlEndpoint().endsWith("\\")){
                Config.getConfig().setUrlEndpoint(Config.getConfig().getUrlEndpoint().substring(0, Config.getConfig().getUrlEndpoint().length()-1));
            }
        }
    }
    private static boolean containsArg(String[] args, String argToFind) {
        for(int i=0; i<args.length; i++) {
            if(args[i].toLowerCase().contains(argToFind.toLowerCase())){
                return true;
            }
        }
        return false;
    }
    private static String getArgValue(String[] args, String argToFind) {
        for(int i=0; i<args.length; i++) {
            if(args[i].toLowerCase().contains(argToFind.toLowerCase())){
                return args[i].substring(args[i].indexOf("=")+1, args[i].length());
            }
        }
        return "";
    }
}
