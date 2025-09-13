package m6a5x98.enhancedeffects;

import m6a5x98.enhancedeffects.datagen.EnhancedEffectsRecipesProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;


public class EnhancedEffectsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();

		pack.addProvider(EnhancedEffectsRecipesProvider::new);
	}
}

