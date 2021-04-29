package com.github.kuzznya.rp.spec;

import com.github.kuzznya.rp.spec.pub.DeferredPublisher;
import com.github.kuzznya.rp.spec.pub.JustPublisher;
import com.github.kuzznya.rp.spec.pub.MapPublisher;
import com.github.kuzznya.rp.spec.pub.ParallelPublisher;
import com.github.kuzznya.rp.spec.sub.CollectingSubscriber;
import com.github.kuzznya.rp.spec.sub.ConsumingSubscriber;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Publisher<T> {

    @SafeVarargs
    static <V> Publisher<V> just(V... values) {
        return new JustPublisher<>(values);
    }

    static <V> Publisher<V> from(Supplier<V> supplier) {
        return Publisher.defer(() -> Publisher.just(supplier.get()));
    }

    static <V> Publisher<V> defer(Supplier<Publisher<V>> supplier) {
        return new DeferredPublisher<>(supplier);
    }

    void subscribe(Subscriber<? super T> subscriber);

    default <R> Publisher<R> map(Function<T, R> mapper) {
        return new MapPublisher<>(this, mapper);
    }

    default Publisher<T> peek(Consumer<T> consumer) {
        return new MapPublisher<>(this, v -> {
            consumer.accept(v);
            return v;
        });
    }

    default Publisher<T> parallel(int parallelism) {
        return new ParallelPublisher<>(this, parallelism);
    }

    default List<T> block() {
        CollectingSubscriber<T> sub = new CollectingSubscriber<>();
        subscribe(sub);
        return sub.blockingGet();
    }

    default void consume(Consumer<T> consumer) {
        ConsumingSubscriber<T> subscriber = new ConsumingSubscriber<>(consumer);
        subscribe(subscriber);
    }
}
