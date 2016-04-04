package com.zurich.mobile.utils.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * Created by weixinfei on 16/3/27.
 */
public interface PausableExecutorService extends ExecutorService {
    public void pause();

    public void resume();

    public BlockingQueue<Runnable> getQueue();
}
