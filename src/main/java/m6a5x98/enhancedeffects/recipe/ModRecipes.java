package m6a5x98.enhancedeffects.recipe;

import m6a5x98.enhancedeffects.EnhancedEffects;
import m6a5x98.enhancedeffects.recipe.recipes.ExtractRecipe;
import m6a5x98.enhancedeffects.recipe.recipes.PoisonedFoodRecipe;
import m6a5x98.enhancedeffects.recipe.recipes.PotionMergeRecipe;
import net.minecraft.recipe.*;
import m6a5x98.enhancedeffects.recipe.recipes.TippedArrowAllPotionsRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static final RecipeSerializer<PoisonedFoodRecipe> POISONED_FOOD =
            Registry.register(
                    Registries.RECIPE_SERIALIZER,
                    new Identifier(EnhancedEffects.MOD_ID,PoisonedFoodRecipe.RecipeType.ID),
                    new SpecialRecipeSerializer<>(PoisonedFoodRecipe::new)
            );
    public static final RecipeSerializer<ExtractRecipe> EXTRACT =
            Registry.register(
                    Registries.RECIPE_SERIALIZER,
                    new Identifier(EnhancedEffects.MOD_ID, ExtractRecipe.RecipeType.ID),
                    new SpecialRecipeSerializer<>(ExtractRecipe::new)
            );
    public static final RecipeSerializer<PotionMergeRecipe> POTION_MERGE =
            Registry.register(
                    Registries.RECIPE_SERIALIZER,
                    new Identifier(EnhancedEffects.MOD_ID, PotionMergeRecipe.RecipeType.ID),
                    new SpecialRecipeSerializer<>(PotionMergeRecipe::new)
            );
    public static final RecipeSerializer<TippedArrowAllPotionsRecipe> TIPPED_ARROW =
            Registry.register(
                    Registries.RECIPE_SERIALIZER,
                    new Identifier(EnhancedEffects.MOD_ID, TippedArrowAllPotionsRecipe.RecipeType.ID),
                    new SpecialRecipeSerializer<>(TippedArrowAllPotionsRecipe::new)
            );
    public static void init() {}
}