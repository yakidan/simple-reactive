package com.github.kuzznya.rp.spec.pub;

import com.github.kuzznya.rp.spec.Publisher;
import com.github.kuzznya.rp.spec.Subscriber;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class DeferredPublisher<T> implements Publisher<T> {

    private final Supplier<Publisher<T>> supplier;

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        supplier.get().subscribe(subscriber);
    }
}
