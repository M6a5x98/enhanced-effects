package m6a5x98.enhancedeffects.datagen;

import m6a5x98.enhancedeffects.item.ModItems;
import m6a5x98.enhancedeffects.recipe.ModRecipes;
import m6a5x98.enhancedeffects.recipe.recipes.ExtractRecipe;
import m6a5x98.enhancedeffects.recipe.recipes.PoisonedFoodRecipe;
import m6a5x98.enhancedeffects.recipe.recipes.PotionMergeRecipe;
import m6a5x98.enhancedeffects.recipe.recipes.TippedArrowAllPotionsRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class EnhancedEffectsRecipesProvider extends FabricRecipeProvider {
    public EnhancedEffectsRecipesProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> consumer) {
        EnhancedEffectsSpecialCraftingProvider.generateCraftingSpecialRecipe(consumer, PoisonedFoodRecipe.RecipeType.ID, ModRecipes.POISONED_FOOD);
        EnhancedEffectsSpecialCraftingProvider.generateCraftingSpecialRecipe(consumer, ExtractRecipe.RecipeType.ID, ModRecipes.EXTRACT);
        EnhancedEffectsSpecialCraftingProvider.generateCraftingSpecialRecipe(consumer, PotionMergeRecipe.RecipeType.ID, ModRecipes.POTION_MERGE);
        EnhancedEffectsSpecialCraftingProvider.generateCraftingSpecialRecipe(consumer, TippedArrowAllPotionsRecipe.RecipeType.ID, ModRecipes.TIPPED_ARROW);
        ShapelessRecipeJsonBuilder
                .create(RecipeCategory.FOOD, ModItems.catalyst)
                .input(Items.PAPER)
                .input(Items.GLOWSTONE_DUST)
                .input(Items.REDSTONE)
                .input(Items.SUGAR)
                .criterion(
                        FabricRecipeProvider.hasItem(Items.REDSTONE),
                        FabricRecipeProvider.conditionsFromItem(Items.REDSTONE)
                )
                .criterion(
                        FabricRecipeProvider.hasItem(Items.GLOWSTONE_DUST),
                        FabricRecipeProvider.conditionsFromItem(Items.GLOWSTONE_DUST)
                )
                .offerTo(consumer);
    }
}
