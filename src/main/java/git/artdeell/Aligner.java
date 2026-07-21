package git.artdeell;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Aligner implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("Aligner");

    @Override
    public void onInitialize() {
        LOGGER.info("Aligner initialized!");
    }
}
