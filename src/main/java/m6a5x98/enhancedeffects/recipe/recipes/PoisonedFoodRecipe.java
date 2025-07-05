package m6a5x98.enhancedeffects.recipe.recipes;

import m6a5x98.enhancedeffects.item.ModItems;
import m6a5x98.enhancedeffects.recipe.ModRecipes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class PoisonedFoodRecipe extends SpecialCraftingRecipe {
    public PoisonedFoodRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(RecipeInputInventory inv, World world) {
        boolean foundFood = false;
        boolean foundPotion = false;
        boolean foundCatalyst = false;
        boolean foodHasEffects = false;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.isEmpty()) continue;
            if (!foundFood && stack.isFood()) {
                foundFood = true;
                foodHasEffects = !stack.getItem()
                        .getFoodComponent()
                        .getStatusEffects()
                        .isEmpty() || !stack.getOrCreateNbt().getList("Effects", NbtElement.COMPOUND_TYPE).isEmpty();
                continue;
            }
            if (!foundPotion && stack.getItem() instanceof PotionItem) {
                foundPotion = true;
                continue;
            }
            if (!foundCatalyst && stack.isOf(ModItems.catalyst)) {
                foundCatalyst = true;
                continue;
            }
            return false;
        }
        if (!foundFood) {
            return false;
        }
        if (foodHasEffects) {
            return foundCatalyst;
        }
        return foundPotion;
    }



    @Override
    public ItemStack craft(RecipeInputInventory inv, net.minecraft.registry.DynamicRegistryManager drm) {
        ItemStack result = ItemStack.EMPTY;
        ItemStack potionStack = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof PotionItem) {
                potionStack = stack;
            } else if (stack.isFood()) {
                result = new ItemStack(stack.getItem(), 1);
            }
        }
        if (result.isEmpty() || potionStack.isEmpty()) {
            return new ItemStack(Items.AIR);
        }
        List<StatusEffectInstance> effects = PotionUtil.getPotionEffects(potionStack);
        //result.getItem().getFoodComponent().getStatusEffects().forEach(foodItemEffect -> effects.add(foodItemEffect.getFirst()));
        NbtCompound root = result.getOrCreateNbt();
        NbtList list = new NbtList();

        for (StatusEffectInstance effect : effects) {
            NbtCompound tag = new NbtCompound();
            int rawId = Registries.STATUS_EFFECT.getRawId(effect.getEffectType());
            tag.putInt("Effect", rawId);
            tag.putInt("Duration", effect.getDuration());
            tag.putByte("Amplifier", (byte) effect.getAmplifier());
            list.add(tag);
        }
        root.put("GivenEffects", list);
        result.setNbt(root);
        return result;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.POISONED_FOOD;
    }

    @Override
    public DefaultedList<ItemStack> getRemainder(RecipeInputInventory inv) {
        DefaultedList<ItemStack> remainders = DefaultedList.ofSize(inv.size(), ItemStack.EMPTY);
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.isOf(ModItems.catalyst)) {
                ItemStack copy = stack.copy();
                copy.setDamage(copy.getDamage() + 1);
                if (copy.getDamage() < copy.getMaxDamage()) {
                    remainders.set(i, copy);
                }
            }
        }
        return remainders;
    }

    public static class RecipeType implements net.minecraft.recipe.RecipeType<PoisonedFoodRecipe> {
        public static final RecipeType INSTANCE = new RecipeType();
        public static final String ID = "crafting_special_poisoned_food";
    }
}
