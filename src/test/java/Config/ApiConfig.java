package Config;
import org.aeonbits.owner.Config;

@Config.LoadPolicy(
Config.LoadType.MERGE
)

@Config.Sources(
       {"system:env",
       "classpath:config.properties"}
)

public interface ApiConfig extends Config {
    @Key("URL")
    String url();

    @Key("API_URL")
    String apiUrl();

    @Key("TIMEOUT")
    @DefaultValue("10000")
    int timeout();

    @Key("API_LOGS")
    @DefaultValue("TRUE")
    boolean apiLogs();;

    @Key("ADMIN_LOGIN")
    String adminLogin();

    @Key("ADMIN_PASSWORD")
    String adminPassword();

    @Key("GOOD_NAME")
    @DefaultValue("BASE_GOOD_NAME")
    String goodName();

    @Key("GOOD_PRICE")
    @DefaultValue("50.0")
    double goodPrice();
}
