package gl1tch.dmgresist;

import gl1tch.dmgresist.util.ModStuffs;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LivingEntityDamageResistance implements ModInitializer {
	public static final String MOD_ID = "living-entity-damage-resistance";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModStuffs.register();
	}
}
