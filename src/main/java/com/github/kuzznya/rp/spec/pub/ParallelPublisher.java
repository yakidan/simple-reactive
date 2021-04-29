package com.github.kuzznya.rp.spec.pub;

import com.github.kuzznya.rp.spec.Publisher;
import com.github.kuzznya.rp.spec.Subscriber;
import com.github.kuzznya.rp.spec.Subscription;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ParallelPublisher<T> implements Publisher<T> {

    private final Publisher<T> upstream;
    private final ThreadPoolExecutor executor;

    public ParallelPublisher(Publisher<T> upstream, int poolSize) {
        this.upstream = upstream;
        executor = new ThreadPoolExecutor(poolSize, poolSize, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        upstream.subscribe(new ParallelSubscriber(subscriber));
    }

    @RequiredArgsConstructor
    private class ParallelSubscriber implements Subscriber<T> {

        private final Subscriber<? super T> subscriber;

        @Override
        public void onSubscribe(Subscription subscription) {
            subscriber.onSubscribe(subscription);
        }

        @Override
        public void onNext(T value) {
            executor.execute(() -> subscriber.onNext(value));
        }

        @Override
        public void onComplete() {
            executor.execute(subscriber::onComplete);
            executor.shutdown();
        }
    }
}
