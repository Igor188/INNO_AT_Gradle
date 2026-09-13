package Config;

import org.aeonbits.owner.ConfigFactory;

import java.io.IOException;
import java.util.Properties;

public class ConfigProvider {
    /*
    String configPath = System.getenv("CONFIG_LOCATION");

    Properties props;

    public ConfigProvider(){
        try {
            props.load(getClass().getResourceAsStream(("config.properties")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }
*/
    public static ApiConfig apiProps =  ConfigFactory.create(ApiConfig.class, System.getProperties(), System.getenv());

    private ConfigProvider() {}

}
