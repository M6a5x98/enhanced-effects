package m6a5x98.enhancedeffects.recipe.recipes;

import com.mojang.serialization.MapCodec;
import m6a5x98.enhancedeffects.EnhancedEffects;
import m6a5x98.enhancedeffects.EnhancedEffectsUtil;
import m6a5x98.enhancedeffects.components.ModComponents;
import m6a5x98.enhancedeffects.recipe.ModRecipes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExtractRecipe extends SpecialCraftingRecipe  {
    public ExtractRecipe() {
        super(CraftingRecipeCategory.MISC);
    }

    @Override
    public boolean matches(CraftingRecipeInput inv, World world) {
        boolean foundBottle = false;
        boolean foundFood = false;
        boolean foodHasEffects = false;
        boolean potionTypeMutator = false;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (!foundFood && EnhancedEffectsUtil.isFood(stack)) {
                foundFood = true;
                foodHasEffects = !EnhancedEffectsUtil.getPotionEffects(stack).isEmpty()
                        || !EnhancedEffectsUtil.getSusStewEffects(stack).isEmpty();
                continue;
            }
            if (!foundBottle && stack.isOf(Items.GLASS_BOTTLE)) {
                foundBottle = true;
                continue;
            }
            if (!potionTypeMutator && (stack.isOf(Items.GUNPOWDER) || stack.isOf(Items.DRAGON_BREATH))) {
                potionTypeMutator = true;
                continue;
            }
            return false;
        }

        return foundBottle && foundFood && foodHasEffects;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput inv, RegistryWrapper.WrapperLookup registries) {
        ItemStack foodStack = ItemStack.EMPTY;
        ItemStack result = new ItemStack(Items.POTION);

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (EnhancedEffectsUtil.isFood(stack)) {
                foodStack = stack.copy();
                continue;
            }
            if (stack.isOf(Items.GUNPOWDER)) {
                result = new ItemStack(Items.SPLASH_POTION);
            }
            if (stack.isOf(Items.DRAGON_BREATH)) {
                result = new ItemStack(Items.LINGERING_POTION);
            }
        }
        List<StatusEffectInstance> foodEffects = EnhancedEffectsUtil.getPotionEffects(foodStack);
        if (foodStack.isOf(Items.SUSPICIOUS_STEW)) {
            foodEffects.addAll(EnhancedEffectsUtil.getSusStewEffects(foodStack));
        }
        EnhancedEffects.LOGGER.info("FoodEffects: {}", foodEffects);
        Text foodName = Text.translatable(foodStack.getItem().getTranslationKey());
        result.set(DataComponentTypes.CUSTOM_NAME, Text.translatable(
                "item.enhanced_effects.extract",
                foodName
        ).setStyle(Style.EMPTY.withItalic(false)));
        result.set(ModComponents.EXTRACTED_FROM_COMPONENT, foodStack.getItem().getTranslationKey());
        result.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(
                Optional.of(Potions.WATER),
                Optional.of(Random.create().nextInt()),
                foodEffects,
                Optional.empty()
        ));
        return result;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return ModRecipes.EXTRACT_SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<ExtractRecipe> {
        @Override
        public MapCodec<ExtractRecipe> codec() {
            return MapCodec.unit(ExtractRecipe::new);
        }


        @Override
        public PacketCodec<RegistryByteBuf, ExtractRecipe> packetCodec() {
            return PacketCodec.of(
                    (buf, value) -> {},
                    buf -> new ExtractRecipe()
            );
        }
    }
}
