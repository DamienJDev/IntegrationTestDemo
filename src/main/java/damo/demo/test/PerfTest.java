package damo.demo.test;

import damo.demo.test.tests.HealthCheckTests;
import damo.demo.test.tests.Result;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PerfTest {

    ConcurrentHashMap<String, Boolean> threadTackingMap = new ConcurrentHashMap();

    List<String> fails = new LinkedList<String>();
    List<String> success = new LinkedList<String>();

    private Date testsStarted;
    private Date testsCompleted;
    private Long longestTest = 0L;
    private Long shortedTest = Long.MAX_VALUE;
    private Long totalTestRunTime = 0L;
    private int numTests = 0;

    public void doPerfTest() {
        testsStarted = new Date();

        System.out.println("Starting up " + Config.getConfig().getPerfTestNumThreads() + " threads with " + Config.getConfig().getPerfTestNumRequests() + " requests to be run on each thread");
        for(int i=0; i<Config.getConfig().getPerfTestNumThreads(); i++) {
            threadTackingMap.put("PerfTestThread-Thread-" + i, false);
            PerfTestThread rt = new PerfTestThread( "PerfTestThread-Thread-" + i, Config.getConfig().getPerfTestNumRequests());
            rt.start();
        }
    }
    private class PerfTestThread extends Thread {
        private Thread t;
        private String threadName;
        int numRequests;

        PerfTestThread(String name, int numRequests) {
            threadName = name;
            this.numRequests = numRequests;
        }

        public void run() {
            numRequests-=1;
            List<Result> results = new LinkedList<Result>();

            HealthCheckTests tests = new HealthCheckTests();
            tests.runHealthCheckTests(results);

            for(Result result: results) {
                if(result==null || result.testName == null || result.startStamp == null || result.endStamp == null){
                    String resultString = "";
                    resultString += this.threadName + ":";
                    resultString += this.numRequests + "-";
                    fails.add(resultString);
                }
                if(!result.excludeFromResults) {
                    long processedTime = -1;
                    try {
                        processedTime = (result.endStamp.getTime() - result.startStamp.getTime());
                    } catch (Exception e){System.out.println("failed to load test results");}
                    if(processedTime>-1) {
                        totalTestRunTime += processedTime;
                        numTests++;
                        if (longestTest < processedTime) {
                            longestTest = processedTime;
                        }
                        if (processedTime < shortedTest) {
                            shortedTest = processedTime;
                        }
                    }
                    String resultString = "";
                    try {
                        resultString += this.threadName + ":";
                        resultString += this.numRequests + "-";
                        resultString += result.testName + "-";
                        resultString += result.result + "  processed in: ";
                        resultString += processedTime + " milli seconds";
                    } catch (Exception e){System.out.println("failed to load test results");}
                    if (!result.result) {
                        fails.add(resultString);
                    } else {
                        success.add(resultString);
                    }
                }
            }

            if(numRequests>0){
                PerfTestThread rt = new PerfTestThread( threadName, numRequests);
                rt.start();
            } else {
                threadTackingMap.put(threadName, true);
                boolean complete = true;
                for (String key : threadTackingMap.keySet()) {
                    if (threadTackingMap.get(key).equals(Boolean.valueOf(false))) {
                        complete = false;
                    }
                }
                if(complete) {
                    testsCompleted = new Date();
                    //show results:
                    System.out.println("--------------- successful results -----------------");
                    for(int i=0; i<success.size(); i++){
                        try {
                            System.out.println(success.get(i));
                        } catch (Exception e){System.out.println("failed to print success item");}
                    }
                    System.out.println("--------------- end of successful results -----------------");
                    System.out.println();
                    System.out.println();
                    System.out.println("--------------- failed results -----------------");
                    for(int i=0; i<fails.size(); i++){
                        try {
                            System.out.println(fails.get(i));
                        } catch (Exception e){System.out.println("failed to print fail item");}
                    }
                    System.out.println("--------------- end of failed results -----------------");


                    System.out.println();
                    System.out.println(Config.getConfig().getPerfTestNumThreads() + " threads with " + Config.getConfig().getPerfTestNumRequests() + " loops run per thread");
                    System.out.println("Tests started at: " + testsStarted);
                    System.out.println("Tests completed at: " + testsCompleted);

                    long testsRunTime;
                    if((testsCompleted.getTime()-testsStarted.getTime())>0) {
                        testsRunTime = (testsCompleted.getTime() - testsStarted.getTime()) / 1000;
                    } else {
                        testsRunTime = 0;
                    }

                    System.out.println("Total time to run tests: " + testsRunTime + " seconds");
                    System.out.println("Shortest test time: " + shortedTest + " milli seconds");
                    System.out.println("Longest test time: " + longestTest + " milli seconds");
                    Long avgTestTime;
                    if(totalTestRunTime>0) {
                        avgTestTime = totalTestRunTime / numTests;
                    } else {
                        avgTestTime = 0L;
                    }
                    System.out.println("total tests: " + numTests);
                    System.out.println("Average test time: " + avgTestTime + " milli seconds");
                }
            }

        }

        public void start () {
            if (t == null) {
                t = new Thread (this, threadName);
                t.start ();
            }
        }
    }
}
