package azmalent.cuneiform.lib.collections;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;

public class WeightedList<T> {
    private static class Node<T> {
        protected final T value;
        protected final int weight;

        private Node(T value, int weight) {
            this.value = value;
            this.weight = weight;
        }
    }

    private final List<Node<T>> items = Lists.newArrayList();
    private int totalWeight = 0;

    public void add(T value, int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("Weight must be positive!");
        } else if (weight == 0) {
            return;
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

    public T getRandomItem(Random random) {
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
