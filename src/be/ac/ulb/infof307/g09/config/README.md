# Config package :

Here you will find the files for everything concerning the configuration part of the application, mainly used to instantiate Mock during the various test.

## Files :

<p>
<u>.properties files</u>: define a build configuration by associating a an interface to the name of a class
</p>
<p>
<u>Configuration</u>: open and read the asked .properties file
</p>
<p>
<u>AbstractConfigurationSingleton</u>: associate the interface to an implementation using the Configuration.class, instanciate the implementation trough introspection and make the implementations accessible through getters.
</p>
<p>
<u>ConfigurationSingleton</u>: extend AbstractConfigurationSingleton and open a configuration depending on the given args (dev.properties is used by default)
</p>
<p>
<u>TestBusinessConfigurationSingleton</u>: extend AbstractConfigurationSingleton and open  the configuration used when testing  the controller (previously called business)
</p>
<p>
<u>TestDAOConfigurationSingleton</u>: extend AbstractConfigurationSingleton and open the configuration used when testing the model
</p>

