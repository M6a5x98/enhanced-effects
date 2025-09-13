package m6a5x98.enhancedeffects.datagen;

import m6a5x98.enhancedeffects.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class EnhancedEffectsRecipesProvider extends FabricRecipeProvider {
    public EnhancedEffectsRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                RegistryWrapper.Impl<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);
                createShapeless(RecipeCategory.MISC, ModItems.CATALYST)
                        .input(Items.PAPER)
                        .input(Items.GLOWSTONE_DUST)
                        .input(Items.REDSTONE)
                        .input(Items.SUGAR)
                        .criterion(
                                hasItem(Items.REDSTONE),
                                conditionsFromItem(Items.REDSTONE)
                        )
                        .criterion(
                                hasItem(Items.GLOWSTONE_DUST),
                                conditionsFromItem(Items.GLOWSTONE_DUST)
                        )
                        .offerTo(exporter);
            }
        };
    }

    @Override
    public String getName() {
        return "EnhancedEffectsRecipesProvider";
    }
}
