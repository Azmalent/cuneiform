package azmalent.cuneiform.util;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public final class ClientUtil {
    public static Level getClientLevel() {
        return Minecraft.getInstance().level;
    }

    public static void addEffectsTooltip(List<MobEffectInstance> effects, List<Component> tooltip) {
        MutableComponent noEffect = (Component.translatable("effect.none")).withStyle(ChatFormatting.GRAY);

        List<Pair<Attribute, AttributeModifier>> list1 = Lists.newArrayList();
        if (effects.isEmpty()) tooltip.add(noEffect);
        else {
            for(MobEffectInstance effectInstance : effects) {
                MutableComponent line = Component.translatable(effectInstance.getEffect().getDescriptionId());
                MobEffect effect = effectInstance.getEffect();
                Map<Attribute, AttributeModifier> map = effect.getAttributeModifiers();
                if (!map.isEmpty()) {
                    for(Map.Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
                        AttributeModifier attributemodifier = entry.getValue();
                        AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierValue(effectInstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                        list1.add(new Pair<>(entry.getKey(), attributemodifier1));
                    }
                }

                if (effectInstance.getAmplifier() > 0) {
                    line = Component.translatable("potion.withAmplifier", line, Component.translatable("potion.potency." + effectInstance.getAmplifier()));
                }
                else if (effectInstance.getDuration() > 20) {
                    line = Component.translatable("potion.withDuration", line, MobEffectUtil.formatDuration(effectInstance, 1));
                }

                tooltip.add(line.withStyle(effect.getCategory().getTooltipFormatting()));
            }
        }

        if (!list1.isEmpty()) {
            tooltip.add(CommonComponents.EMPTY);
            tooltip.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));

            for(Pair<Attribute, AttributeModifier> pair : list1) {
                AttributeModifier modifier = pair.getSecond();
                double amount = modifier.getAmount();
                if (amount == 0) continue;

                double d1 = modifier.getAmount();
                if (modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE || modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_TOTAL) {
                    d1 *= 100.0D;
                }

                if (amount < 0.0D) d1 *= -1.0D;

                tooltip.add((
                                Component.translatable(
                                    String.format("attribute.modifier.%s.%d", amount > 0 ? "plus" : "take", modifier.getOperation().toValue()),
                                    ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1),
                                    Component.translatable(pair.getFirst().getDescriptionId())
                                )
                            ).withStyle(amount > 0 ? ChatFormatting.BLUE : ChatFormatting.RED));
            }
        }
    }
}
