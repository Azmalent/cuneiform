package azmalent.cuneiform.lib.util;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unused")
public final class TextUtil {
    public static String splitCamelCase(String string) {
        String[] words = StringUtils.splitByCharacterTypeCamelCase(string);
        if (words.length > 0) {
            words[0] = StringUtils.capitalize(words[0]);
        }

        return StringUtils.join(words, ' ');
    }

    public static void addEffectsTooltip(List<MobEffectInstance> effects, List<Component> tooltip) {
        addEffectsTooltip(effects, tooltip, 1);
    }

    public static void addEffectsTooltip(List<MobEffectInstance> effects, List<Component> tooltip, float durationMultiplier) {
        if (effects.isEmpty()) {
            MutableComponent noEffect = (new TranslatableComponent("effect.none")).withStyle(ChatFormatting.GRAY);
            tooltip.add(noEffect);
            return;
        }

        List<Pair<Attribute, AttributeModifier>> attributeModifiers = Lists.newArrayList();

        for(MobEffectInstance effectInstance : effects) {
            MutableComponent line = new TranslatableComponent(effectInstance.getDescriptionId());
            MobEffect effect = effectInstance.getEffect();
            Map<Attribute, AttributeModifier> map = effect.getAttributeModifiers();
            if (!map.isEmpty()) {
                for(Map.Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
                    AttributeModifier attributemodifier = entry.getValue();
                    AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierValue(effectInstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                    attributeModifiers.add(new Pair<>(entry.getKey(), attributemodifier1));
                }
            }

            if (effectInstance.getAmplifier() > 0) {
                line = new TranslatableComponent("potion.withAmplifier", line, new TranslatableComponent("potion.potency." + effectInstance.getAmplifier()));
            }
            else if (effectInstance.getDuration() > 20) {
                line = new TranslatableComponent("potion.withDuration", line, MobEffectUtil.formatDuration(effectInstance, durationMultiplier));
            }

            tooltip.add(line.withStyle(effect.getCategory().getTooltipFormatting()));
        }

        if (!attributeModifiers.isEmpty()) {
            tooltip.add(TextComponent.EMPTY);
            tooltip.add((new TranslatableComponent("potion.whenDrank")).withStyle(ChatFormatting.DARK_PURPLE));

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
                    new TranslatableComponent(
                        String.format("attribute.modifier.%s.%d", amount > 0 ? "plus" : "take", modifier.getOperation().toValue()),
                        ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1),
                        new TranslatableComponent(pair.getFirst().getDescriptionId())
                    )
                ).withStyle(amount > 0 ? ChatFormatting.BLUE : ChatFormatting.RED));
            }
        }
    }
}
