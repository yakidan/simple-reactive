package com.github.kuzznya.rp;

import com.github.kuzznya.rp.spec.Publisher;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class App {
    public static void main(String[] args) {
        Publisher.just(1, 2, 3)
                .map(i -> i - 1)
                .peek(System.out::println); // ничего не выведет

        List<Integer> result = Publisher.just(1, 2, 3)
                .map(i -> i + 1)
                .peek(System.out::println)
                .collect();
        System.out.println(result);

        var resultFlatMap =
            Publisher.just(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9)).flatMap().collect();
        System.out.println(resultFlatMap);

        var p = Publisher.from(() -> {
            System.out.println("I was called");
            return "Test";
        });
        System.out.println("I was called earlier");
        var result2 = p.collect();
        System.out.println(result2);

        log.info("Creating parallel publisher");
        Publisher.just(1, 2, 3)
                .parallel(3)
                .map(i -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return i;
                })
                .peek(v -> log.info("{}", v))
                .parallel(1)
                .map(i -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return i;
                })
                .peek(v -> log.info("{}", v))
                .collect();
    }
}
