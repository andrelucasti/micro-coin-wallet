package com.crypto.coinwallet.andrelucas.business;

import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    public void execute(Runnable runnable){
        Thread thread = Thread.ofVirtual().name("asyncService-virtual").unstarted(runnable);
        thread.start();
    }
}