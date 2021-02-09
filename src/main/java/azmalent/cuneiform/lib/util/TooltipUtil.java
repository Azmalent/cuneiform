package azmalent.cuneiform.lib.util;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unused")
public final class TooltipUtil {
    public static void addEffectsTooltip(List<EffectInstance> effects, List<ITextComponent> tooltip) {
        addEffectsTooltip(effects, tooltip, 1);
    }

    public static void addEffectsTooltip(List<EffectInstance> effects, List<ITextComponent> tooltip, float durationMultiplier) {
        if (effects.isEmpty()) {
            IFormattableTextComponent noEffect = (new TranslationTextComponent("effect.none")).mergeStyle(TextFormatting.GRAY);
            tooltip.add(noEffect);
            return;
        }

        List<Pair<Attribute, AttributeModifier>> attributeModifiers = Lists.newArrayList();

        for(EffectInstance effectInstance : effects) {
            IFormattableTextComponent line = new TranslationTextComponent(effectInstance.getEffectName());
            Effect effect = effectInstance.getPotion();
            Map<Attribute, AttributeModifier> map = effect.getAttributeModifierMap();
            if (!map.isEmpty()) {
                for(Map.Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
                    AttributeModifier attributemodifier = entry.getValue();
                    AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierAmount(effectInstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                    attributeModifiers.add(new Pair<>(entry.getKey(), attributemodifier1));
                }
            }

            if (effectInstance.getAmplifier() > 0) {
                line = new TranslationTextComponent("potion.withAmplifier", line, new TranslationTextComponent("potion.potency." + effectInstance.getAmplifier()));
            }
            else if (effectInstance.getDuration() > 20) {
                line = new TranslationTextComponent("potion.withDuration", line, EffectUtils.getPotionDurationString(effectInstance, durationMultiplier));
            }

            tooltip.add(line.mergeStyle(effect.getEffectType().getColor()));
        }

        if (!attributeModifiers.isEmpty()) {
            tooltip.add(StringTextComponent.EMPTY);
            tooltip.add((new TranslationTextComponent("potion.whenDrank")).mergeStyle(TextFormatting.DARK_PURPLE));

            for(Pair<Attribute, AttributeModifier> pair : attributeModifiers) {
                AttributeModifier modifier = pair.getSecond();
                double amount = modifier.getAmount();
                if (amount == 0) continue;

                double d1 = modifier.getAmount();
                if (modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE || modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_TOTAL) {
                    d1 *= 100.0D;
                }

                if (amount < 0.0D) d1 *= -1.0D;

                tooltip.add((
                    new TranslationTextComponent(
                        String.format("attribute.modifier.%s.%i", amount > 0 ? "plus" : "take", modifier.getOperation().getId()),
                        ItemStack.DECIMALFORMAT.format(d1),
                        new TranslationTextComponent(pair.getFirst().getAttributeName())
                    )
                ).mergeStyle(amount > 0 ? TextFormatting.BLUE : TextFormatting.RED));
            }
        }
    }
}
