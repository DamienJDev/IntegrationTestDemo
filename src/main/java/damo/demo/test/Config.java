package damo.demo.test;

public class Config {
    private static Config config;

    private boolean verboseResults = false;
    private boolean doPerfTests = false;
    
    private int perfTestNumThreads = 5;
    private int perfTestNumRequests = 10;
    
    private String urlEndpoint = "http://localhost:8080";
    private String runSpecificTest;
    private Config() {}
    public static Config getConfig() {
        if(config==null){
            config = new Config();
        }
        return config;
    }
    public void setVerboseResults(boolean verbose) {
        config.verboseResults = verbose;
    }
    public boolean getVerboseResults() {
        return config.verboseResults;
    }
    public void setUrlEndpoint(String endpointConfig){
        config.urlEndpoint = endpointConfig;
    }
    public String getUrlEndpoint(){
        return config.urlEndpoint;
    }
    public void setRunSpecificTest(String testName){
        config.runSpecificTest = testName;
    }
    public String getRunSpecificTest() {
        return config.runSpecificTest;
    }
    public void setDoPerfTests(boolean doPerfTests) {
    	config.doPerfTests = doPerfTests;
    }
    public boolean getDoPerfTests() {
    	return config.doPerfTests;
    }
    public int getPerfTestNumThreads(){
        return config.perfTestNumThreads;
    }
    public void setPerfTestNumThreads(int perfTestNumThreads){
        config.perfTestNumThreads = perfTestNumThreads;
    }
    public int getPerfTestNumRequests(){
        return config.perfTestNumRequests;
    }
    public void setPerfTestNumRequests(int perfTestNumRequests){
        config.perfTestNumRequests = perfTestNumRequests;
    }
    public boolean runAllTests() {
        if(config.runSpecificTest==null || config.runSpecificTest.equalsIgnoreCase("")){
            return true;
        } else {
            return false;
        }
    }
}
