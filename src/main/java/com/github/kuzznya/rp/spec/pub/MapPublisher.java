package com.github.kuzznya.rp.spec.pub;

import com.github.kuzznya.rp.spec.Publisher;
import com.github.kuzznya.rp.spec.Subscriber;
import com.github.kuzznya.rp.spec.Subscription;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class MapPublisher<T, R> implements Publisher<R> {

    private final Publisher<T> source;
    private final Function<T, R> mapper;

    @Override
    public void subscribe(Subscriber<? super R> subscriber) {
        source.subscribe(new MappingSubscriber(subscriber));
    }

    @RequiredArgsConstructor
    private class MappingSubscriber implements Subscriber<T> {

        private final Subscriber<? super R> subscriber;

        @Override
        public void onSubscribe(Subscription subscription) {
            subscriber.onSubscribe(subscription);
        }

        @Override
        public void onNext(T value) {
            subscriber.onNext(mapper.apply(value));
        }

        @Override
        public void onComplete() {
            subscriber.onComplete();
        }
    }
}
