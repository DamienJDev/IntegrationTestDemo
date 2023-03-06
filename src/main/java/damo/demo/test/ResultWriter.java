package damo.demo.test;

import damo.demo.test.tests.Result;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ResultWriter {
    List<Result> results;
    File failFile = new File("./failed");
    File successfile = new File("./success");
    public ResultWriter(List<Result> results) {
        if(failFile.exists()){
            failFile.delete();
        }
        if(successfile.exists()){
            successfile.delete();
        }
        this.results = results;
    }

    public void writeToConsole() throws IOException {
        List<String> fails = new LinkedList<String>();
        boolean failedTest = false;
        int failCount = 0;
        int successCount = 0;
        int testsRun = 0;
        for(Result result: results) {
            if(!result.excludeFromResults) {
                testsRun++;
            }
        }
        System.out.println("Total tests run: " + testsRun);

        System.out.println("------------- tests passed -------------");
        for(Result result: results) {
            if(!result.excludeFromResults) {
                System.out.println(result.testName + " - " + result.result);
                if (!result.result) {
                    failedTest = true;
                    failCount++;
                    fails.add(result.testName);
                } else {
                    successCount++;
                }
            }
        }
        System.out.println("------------- end of results -------------");
        if (failedTest) {
            System.out.println("Completed with failures");
            System.out.println();
        } else {
            System.out.println("Completed with no failures");
        }

        if (fails.size() > 0) {
            System.out.println("Failed tests:");
            for (int i = 0; i < fails.size(); i++) {
                System.out.println(fails.get(i));
            }
        }

        System.out.println();
        //write a file for scripting to detect and handle
        if (failedTest) {
            System.out.println("Creating fail marker file at: " + failFile.getCanonicalPath());
            failFile.createNewFile();
        } else {
            System.out.println("Creating success marker file at: " + successfile.getCanonicalPath());
            successfile.createNewFile();
        }

        System.out.println(successCount + " completed successfully, " + failCount + " failed");
    }
}
