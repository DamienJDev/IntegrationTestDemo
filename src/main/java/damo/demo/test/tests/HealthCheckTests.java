package damo.demo.test.tests;

import damo.demo.test.Config;
import damo.demo.test.rest.Rest;
import java.util.Date;
import java.util.List;

public class HealthCheckTests {

    public void runHealthCheckTests(List<Result> results) {
        try {
            HealthCheckTests healthCheckTest = new HealthCheckTests();
            results.add(healthCheckTest.testDemoPing());
            results.add(healthCheckTest.testDemoHealthCheckTdv());
        } catch (Exception e) {
            System.out.println("Unexpected exception in HealthCheckTests: " + e);
        }
    }

    Rest rest = new Rest();
    public Result testDemoPing() {
        Result result = new Result();
        result.startStamp = new Date();
        result.testName = new Object(){}.getClass().getEnclosingMethod().getName();
        //check if we're running this test or not, exit if not
        if(!Config.getConfig().runAllTests() && !Config.getConfig().getRunSpecificTest().equalsIgnoreCase(result.testName)){
            result.excludeFromResults = true;
            return result;
        }
        //run the test
        if (Config.getConfig().getVerboseResults())
            System.out.println("------------------- " + result.testName + " start -------------------");
        try {
            Rest.Response response = rest.doGet(Config.getConfig().getUrlEndpoint() + "/system/healthcheck/ping");
            if(response.responseCode == 200 && response.responseContent.equals("demo server ok")){
                if (Config.getConfig().getVerboseResults())
                    System.out.println("------------------- " + result.testName + " end -------------------");
                result.result = true;
            }
        } catch (Exception e) {
            if (Config.getConfig().getVerboseResults()) {
                System.out.println("Exception in " + result.testName + ": " + e);
                e.printStackTrace();
            }
        }
        if (Config.getConfig().getVerboseResults())
            System.out.println("------------------- " + result.testName + " end -------------------");
        result.endStamp = new Date();
        return result;
    }

    public Result testDemoHealthCheckTdv() {
        Result result = new Result();
        result.startStamp = new Date();
        result.testName = new Object(){}.getClass().getEnclosingMethod().getName();
        //check if we're running this test or not, exit if not
        if(!Config.getConfig().runAllTests() && !Config.getConfig().getRunSpecificTest().equalsIgnoreCase(result.testName)){
            result.excludeFromResults = true;
            return result;
        }
        //run the test
        if (Config.getConfig().getVerboseResults())
            System.out.println("------------------- " + result.testName + " start -------------------");
        try {
            Rest.Response response = rest.doGet(Config.getConfig().getUrlEndpoint() + "/system/healthcheck/tdv");
            if(response.responseCode == 200){
                if (Config.getConfig().getVerboseResults())
                    System.out.println("------------------- " + result.testName + " end -------------------");
                result.result = true;
            }
        } catch (Exception e) {
            if (Config.getConfig().getVerboseResults()) {
                System.out.println("Exception in " + result.testName + ": " + e);
                e.printStackTrace();
            }
        }
        if (Config.getConfig().getVerboseResults())
            System.out.println("------------------- " + result.testName + " end -------------------");
        result.endStamp = new Date();
        return result;
    }
}
