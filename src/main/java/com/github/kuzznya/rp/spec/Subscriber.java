package com.github.kuzznya.rp.spec;

public interface Subscriber<T> {
    void onSubscribe(Subscription subscription);
    void onNext(T value);
    void onComplete();
}
