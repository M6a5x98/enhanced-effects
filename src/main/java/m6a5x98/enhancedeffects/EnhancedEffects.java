package m6a5x98.enhancedeffects;

import m6a5x98.enhancedeffects.components.ModComponents;
import m6a5x98.enhancedeffects.item.ModItems;
import m6a5x98.enhancedeffects.recipe.ModRecipes;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnhancedEffects implements ModInitializer {
	public static final String MOD_ID = "enhanced_effects";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModRecipes.init();
		ModComponents.init();
	}
}