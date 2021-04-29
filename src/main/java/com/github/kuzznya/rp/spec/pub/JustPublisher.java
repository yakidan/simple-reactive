package com.github.kuzznya.rp.spec.pub;

import com.github.kuzznya.rp.spec.Publisher;
import com.github.kuzznya.rp.spec.Subscriber;
import com.github.kuzznya.rp.spec.Subscription;
import lombok.RequiredArgsConstructor;

public class JustPublisher<T> implements Publisher<T> {

    private final T[] values;

    public JustPublisher(T[] values) {
        this.values = values;
    }

    public void subscribe(Subscriber<? super T> subscriber) {
        Subscription subscription = new JustSubscription(subscriber);
        subscriber.onSubscribe(subscription);
    }

    @RequiredArgsConstructor
    private class JustSubscription implements Subscription {

        private final Subscriber<? super T> subscriber;
        private int position = 0;

        @Override
        public void request(int n) {
            for (int i = 0; i < n; i++) {
                if (position == values.length) {
                    subscriber.onComplete();
                    return;
                }
                subscriber.onNext(values[position++]);
            }
        }
    }
}
