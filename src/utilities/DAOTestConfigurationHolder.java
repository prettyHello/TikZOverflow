package utilities;

public class DAOTestConfigurationHolder extends AbstractTestConfigurationHolder {
    //this method is called the first time the class is loaded
    {
        super.conf = new ConfigurationSingleton("TestDAO");
    }

}