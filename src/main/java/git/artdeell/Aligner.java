package git.artdeell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ModInitializer;

public class Aligner implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("Aligner");

    @Override
    public void onInitialize() {
        LOGGER.info("Aligner loaded.");
    }
}
