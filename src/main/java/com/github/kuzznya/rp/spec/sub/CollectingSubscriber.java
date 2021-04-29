package com.github.kuzznya.rp.spec.sub;

import com.github.kuzznya.rp.spec.Subscriber;
import com.github.kuzznya.rp.spec.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CollectingSubscriber<T> implements Subscriber<T> {

    private Subscription subscription;
    private final List<T> result = new ArrayList<>();
    private volatile boolean completed = false;

    @Override
    public void onSubscribe(Subscription subscription) {
        if (this.subscription != null)
            throw new RuntimeException("Subscriber already subscribed");
        this.subscription = subscription;
    }

    @Override
    public void onNext(T value) {
        result.add(value);
    }

    @Override
    public void onComplete() {
        completed = true;
    }

    public List<T> blockingGet() {
        Objects.requireNonNull(subscription, "No subscription found");
        subscription.request(Integer.MAX_VALUE);
        while (!completed) {
            Thread.onSpinWait();
        }
        return result;
    }
}
