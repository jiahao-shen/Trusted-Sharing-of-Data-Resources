package com.trustchain.service;


import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class HelloService {
    @Async
    public void testAsync() {
        try {
            Thread.sleep(10 * 1000);
            System.out.println("Hello World?");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    public Future<Boolean> testAsyncResult() {
        try {
            Thread.sleep(10 * 1000);
            return new AsyncResult<>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new AsyncResult<>(false);
        }
    }
}
