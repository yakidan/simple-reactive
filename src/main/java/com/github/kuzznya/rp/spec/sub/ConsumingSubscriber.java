package com.github.kuzznya.rp.spec.sub;

import com.github.kuzznya.rp.spec.Subscriber;
import com.github.kuzznya.rp.spec.Subscription;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class ConsumingSubscriber<T> implements Subscriber<T> {

    private final Consumer<? super T> consumer;
    private Subscription subscription;

    @Override
    public void onSubscribe(Subscription subscription) {
        if (this.subscription != null)
            throw new RuntimeException("Subscriber already subscribed");
        this.subscription = subscription;
    }

    @Override
    public void onNext(T value) {
        consumer.accept(value);
    }

    @Override
    public void onComplete() {

    }
}
