package m6a5x98.enhancedeffects.recipe;

import m6a5x98.enhancedeffects.EnhancedEffects;
import m6a5x98.enhancedeffects.recipe.recipes.ExtractRecipe;
import m6a5x98.enhancedeffects.recipe.recipes.PoisonedFoodRecipe;
import m6a5x98.enhancedeffects.recipe.recipes.PotionMergeRecipe;
import m6a5x98.enhancedeffects.recipe.recipes.TippedArrowAllPotionsRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, EnhancedEffects.MOD_ID);
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PoisonedFoodRecipe>> POISONED_FOOD_SERIALIZER =
            SERIALIZERS.register(
                    "crafting_poisoned_food",
                    PoisonedFoodRecipe.Serializer::new
            );

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ExtractRecipe>> EXTRACT_SERIALIZER =
            SERIALIZERS.register(
                    "crafting_extract",
                    ExtractRecipe.Serializer::new
            );

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PotionMergeRecipe>> POTION_MERGE_SERIALIZER =
            SERIALIZERS.register(
                    "crafting_potion_merge",
                    PotionMergeRecipe.Serializer::new
            );


    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TippedArrowAllPotionsRecipe>> TIPPED_ARROW_SERIALIZER =
            SERIALIZERS.register(
                    "crafting_tipped_arrow",
                    TippedArrowAllPotionsRecipe.Serializer::new
            );


    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
