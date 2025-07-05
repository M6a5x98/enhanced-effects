package m6a5x98.enhancedeffects.datagen;

import m6a5x98.enhancedeffects.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;


public class EnhancedEffectsModelsProvider extends FabricModelProvider {
    public EnhancedEffectsModelsProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.catalyst, "_4", Models.GENERATED);
        itemModelGenerator.register(ModItems.catalyst, "_3", Models.GENERATED);
        itemModelGenerator.register(ModItems.catalyst, "_2", Models.GENERATED);
        itemModelGenerator.register(ModItems.catalyst, "_1", Models.GENERATED);
    }
}
