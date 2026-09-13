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
    public String url();

    @Key("MODE")
    public String mode();

    @Key("GOOD_NAME")
    public String GOOD_NAME();
}
