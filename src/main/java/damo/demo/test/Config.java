package damo.demo.test;

public class Config {
    private static Config config;

    private boolean verboseResults = false;
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
    public boolean runAllTests() {
        if(config.runSpecificTest==null || config.runSpecificTest.equalsIgnoreCase("")){
            return true;
        } else {
            return false;
        }
    }
}
