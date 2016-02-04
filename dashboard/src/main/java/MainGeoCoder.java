import java.util.logging.Level;
import java.util.logging.Logger;

import org.consumersunion.stories.server.helper.geo.FullGeoCoder;
import org.consumersunion.stories.server.util.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

public class MainGeoCoder {
    private final static Logger logger = Logger.getLogger(MainGeoCoder.class.getName());
    private static final Integer DEFAULT_QUOTA = 2000;

    public static void main(String[] args) throws Exception {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();

        logger.log(Level.INFO, "GeoCoding Addresses...");
        FullGeoCoder geoCoder = context.getBean(FullGeoCoder.class);
        geoCoder.geoCodeAll(DEFAULT_QUOTA);
        logger.log(Level.INFO, "GeoCoding Finised...");

        System.exit(0);
    }
}
