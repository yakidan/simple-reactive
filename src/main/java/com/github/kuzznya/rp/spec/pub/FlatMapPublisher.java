package com.github.kuzznya.rp.spec.pub;

import com.github.kuzznya.rp.spec.Publisher;
import com.github.kuzznya.rp.spec.Subscriber;
import com.github.kuzznya.rp.spec.Subscription;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FlatMapPublisher<T, R> implements Publisher<R> {

    private final Publisher<T> source;

    @Override
    public void subscribe(Subscriber<? super R> subscriber) {
        source.subscribe(new FlatMapSubscriber(subscriber));
    }

    @RequiredArgsConstructor
    private class FlatMapSubscriber implements Subscriber<T> {

        private final Subscriber<? super R> subscriber;

        @Override
        public void onSubscribe(Subscription subscription) {
            subscriber.onSubscribe(subscription);
        }

        @Override
        public void onNext(T value) {
            var values = (Iterable<? extends Object>) value;
            for (Object v : values) {
                subscriber.onNext((R) v);
            }
        }

        @Override
        public void onComplete() {
            subscriber.onComplete();
        }
    }
}
