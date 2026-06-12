package azmalent.cuneiform.config.options;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.google.common.collect.Maps;
import net.minecraft.Util;

import java.util.Map;
import java.util.function.Consumer;

/**
 * A config option that holds a map of key-value pairs, stored as a NightConfig
 * {@link Config} object.
 *
 * <p>Provides a {@link #getMap()} convenience method to access the underlying
 * map with proper typing.</p>
 *
 * @param <TValue> the value type of the map
 */
public final class MapOption<TValue> extends BasicOption<Config> {
    @SuppressWarnings("unchecked")
    private MapOption(Map<String, TValue> defaultValue) {
        super(CommentedConfig.wrap((Map<String, Object>) defaultValue, TomlFormat.instance()));
    }

    /**
     * Creates a map option wrapping the given default map.
     */
    public static <TValue> MapOption<TValue> of(Map<String, TValue> defaultValue) {
        return new MapOption<TValue>(defaultValue);
    }

    /**
     * Creates a map option with a map initialized by the given consumer.
     */
    public static <TValue> MapOption<TValue> of(Map<String, TValue> map, Consumer<Map<String, TValue>> initializer) {
        return of(Util.make(map, initializer));
    }

    /**
     * Creates a map option with a new {@link java.util.TreeMap} initialized by the given consumer.
     */
    public static <TValue> MapOption<TValue> of(Consumer<Map<String, TValue>> initializer) {
        return of(Util.make(Maps.newTreeMap(), initializer));
    }

    /**
     * Creates an empty map option backed by a {@link java.util.TreeMap}.
     */
    public static <TValue> MapOption<TValue> empty() {
        return of(Maps.newTreeMap());
    }

    /**
     * Returns the current map value with proper typing.
     */
    @SuppressWarnings("unchecked")
    public Map<String, TValue> getMap() {
        return (Map<String, TValue>) get().valueMap();
    }

    /**
     * Gets the value associated with the given key.
     *
     * @return the value, or {@code null} if not present
     */
    public TValue get(String key) {
        return get().get(key);
    }

    /**
     * Adds a key-value pair to the map.
     *
     * @return {@code true} if the key was newly added, {@code false} if it already existed
     */
    public boolean put(String key, TValue value) {
        return get().add(key, value);
    }

    /**
     * Checks whether the map contains the given key.
     *
     * @return {@code true} if the key is present
     */
    public boolean contains(String key) {
        return get().contains(key);
    }
}
