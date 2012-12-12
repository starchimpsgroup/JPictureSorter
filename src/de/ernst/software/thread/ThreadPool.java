package de.ernst.software.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: cernst
 * Date: 24.03.12
 * Time: 18:40
 */
public class ThreadPool extends ThreadPoolExecutor {
    private static final ThreadPool instance = new ThreadPool(Runtime.getRuntime().availableProcessors());

    public static ThreadPool getInstance() {
        return instance;
    }

    private ThreadPool(final int nThreads) {
        super(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }
}
