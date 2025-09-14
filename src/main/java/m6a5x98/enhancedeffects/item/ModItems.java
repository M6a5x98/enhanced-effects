package m6a5x98.enhancedeffects.item;

import m6a5x98.enhancedeffects.EnhancedEffects;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EnhancedEffects.MOD_ID);

    public static final DeferredItem<Item> CATALYST = ITEMS.register("catalyst", () -> new Item(new Item.Properties().stacksTo(64).durability(5)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
