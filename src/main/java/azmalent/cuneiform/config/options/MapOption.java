package azmalent.cuneiform.config.options;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.google.common.collect.Maps;
import net.minecraft.Util;

import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public final class MapOption<TValue> extends BasicConfigOption<Config> {
    @SuppressWarnings("unchecked")
    private MapOption(Map<String, TValue> defaultValue) {
        super(Config.wrap((Map<String, Object>) defaultValue, TomlFormat.instance()));
    }

    public static <TValue> MapOption<TValue> of(Map<String, TValue> defaultValue) {
        return new MapOption<TValue>(defaultValue);
    }

    public static <TValue> MapOption<TValue> of(Consumer<Map<String, TValue>> initializer) {
        return of(Util.make(Maps.newHashMap(), initializer));
    }

    @SuppressWarnings("unchecked")
    public Map<String, TValue> getMap() {
        return (Map<String, TValue>) get().valueMap();
    }

    public TValue get(String key) {
        return get().get(key);
    }

    public boolean put(String key, TValue value) {
        return get().add(key, value);
    }

    public boolean contains(String key) {
        return get().contains(key);
    }
}
