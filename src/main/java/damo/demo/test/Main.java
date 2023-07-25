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

        if(!Config.getConfig().getDoPerfTests()) {
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
        } else {
            System.out.println("Starting performance tests, warning - this may take several minutes");
            PerfTest test = new PerfTest();
            test.doPerfTest();
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
        System.out.println("to run tests as performance tests:");
        System.out.println("-perf");
        System.out.println("-threads=<number of threads to run> - defaults to 5, only used with -perf flag");
        System.out.println("-requests=<number of requests per thread to try before stopping> - defaults to 10, only used with -perf flag");
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
        if(containsArg(args, "-perf")) {
        	Config.getConfig().setDoPerfTests(containsArg(args, "-perf"));
	        if(containsArg(args, "-threads")) {
	        	try {
		        	Config.getConfig().setPerfTestNumThreads(Integer.parseInt(getArgValue(args, "-threads")));
		            System.out.println(Config.getConfig().getPerfTestNumThreads() + " threads set for performance test");
	            }catch (NumberFormatException e) {
	                System.out.println("Failed to parse -threads value, defaulting to 5, supplied value was: " + getArgValue(args, "-threads"));
	            }
	            if(Config.getConfig().getPerfTestNumThreads()<0){
	                System.out.println("-requests value cannot be negative, defaulting to 5");
	                Config.getConfig().setPerfTestNumThreads(5);
	            }
	        }
	
	        if(containsArg(args, "-requests")) {
	            try {
	            	Config.getConfig().setPerfTestNumRequests(Integer.parseInt(getArgValue(args, "-requests")));
	                System.out.println(Config.getConfig().getPerfTestNumRequests() + " requests per thread set for performance test");
	            }catch (NumberFormatException e) {
	                System.out.println("Failed to parse -requests value, defaulting to 10, supplied value was: " + getArgValue(args, "-requests"));
	            }
	            if(Config.getConfig().getPerfTestNumRequests()<0){
	                System.out.println("-requests value cannot be negative, defaulting to 10");
	                Config.getConfig().setPerfTestNumRequests(50);
	            }
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
