package m6a5x98.enhancedeffects.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.TippedArrowItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Item.class)
public class TippedArrowItemMixin {
    @Inject(method = "getName()Lnet/minecraft/text/Text;", at = @At("HEAD"), cancellable = true)
    private void getName(CallbackInfoReturnable<Text> cir) {
        Item This = (Item)(Object)this;
        if (This instanceof TippedArrowItem && Objects.equals(This.getTranslationKey(), "item.minecraft.tipped_arrow.effect.empty")) {
            cir.setReturnValue(Text.translatable("item.enhanced-effects.custom-tipped-arrow").setStyle(Style.EMPTY.withItalic(false)));
        }
    }

    @Inject(at = @At("HEAD"), method = "getName(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/text/Text;", cancellable = true)
    private void getName(ItemStack stack, CallbackInfoReturnable<Text> cir) {
        if (stack.isOf(Items.TIPPED_ARROW) && Objects.equals(stack.getTranslationKey(), "item.minecraft.tipped_arrow.effect.empty") && PotionUtil.getPotionEffects(stack).size() > 1) {
            cir.setReturnValue(Text.translatable("item.enhanced-effects.custom-tipped-arrow").setStyle(Style.EMPTY.withItalic(false)));
        }
    }
}
