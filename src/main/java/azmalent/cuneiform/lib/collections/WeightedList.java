package azmalent.cuneiform.lib.collections;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class WeightedList<T> {
    private record Node<T>(T value, int weight) { }

    private final List<Node<T>> items = Lists.newArrayList();
    private int totalWeight = 0;

    public void add(T value, int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("Weight must be positive!");
        } else if (weight == 0) {
            return;
        }

        for (Node<T> item : items) {
            if (item.value.equals(value)) {
                items.remove(item);
                weight += item.weight;
                break;
            }
        }

        items.add(new Node<>(value, weight));
        totalWeight += weight;
    }

    public boolean remove(T value) {
        for (Node<T> item : items) {
            if (item.value.equals(value)) {
                items.remove(item);
                totalWeight -= item.weight;
                return true;
            }
        }

        return false;
    }

    public boolean isEmpty() {
        return totalWeight == 0;
    }

    public T sample(Random random) {
        if (isEmpty()) return null;

        int i = random.nextInt(totalWeight);
        for (Node<T> item : items) {
            if (i < item.weight) {
                return item.value;
            }

            i -= item.weight;
        }

        throw new AssertionError("Method didn't return a value!");
    }
}
