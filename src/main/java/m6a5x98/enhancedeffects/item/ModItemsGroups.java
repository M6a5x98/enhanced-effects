package m6a5x98.enhancedeffects.item;

import m6a5x98.enhancedeffects.EnhancedEffects;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemsGroups {
    public static final ItemGroup GROUP = Registry.register(
            Registries.ITEM_GROUP, new Identifier(EnhancedEffects.MOD_ID, "catalyst"),
            FabricItemGroup
                    .builder()
                    .displayName(Text.translatable("itemgroup.catalyst"))
                    .icon(() -> new ItemStack(ModItems.catalyst))
                    .entries(((displayContext, entries) -> {
                        entries.add(ModItems.catalyst);
                    })).build());
    public static void registerItemGroups() {}
}
