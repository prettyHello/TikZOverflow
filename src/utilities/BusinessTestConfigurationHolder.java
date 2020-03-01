/**
 * This class extend AbstractTestConfigurationHolder which uses a singleton to ensure that each conf is loaded only once
 * This class load the configuration "TestBusiness", meant to be used when testing the business
 */
public class BusinessTestConfigurationHolder extends AbstractTestConfigurationHolder {
    //this method is called the first time the class is loaded
     {
        super.conf = new ConfigurationSingleton("TestBusiness");
    }




}
