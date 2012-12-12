package de.ernst.software.thread;

import org.apache.log4j.Logger;
import sun.awt.Mutex;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Created by IntelliJ IDEA.
 * User: cernst
 * Date: 18.03.12
 * Time: 11:10
 */
public class AutoThread {
    private static final Logger logger = Logger.getLogger(AutoThread.class);

    private final ExecutorService threadPool;
    private final Map<String, Integer> running = new HashMap<>();

    public AutoThread(final ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    protected void run(final String name, final Object... args) {
        final Method[] methods = this.getClass().getDeclaredMethods();
        for (final Method method : methods) {
            if (method.isAnnotationPresent(Thread.class) && method.getAnnotation(Thread.class).value().equals(name)) {
                if (method.getReturnType().equals(void.class)) {
                    increment(name);
                    threadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (!method.isAccessible()) {
                                    method.setAccessible(true);
                                }
                                method.invoke(AutoThread.this, args);
                            } catch (InvocationTargetException | IllegalAccessException e) {
                                logger.error(e.getMessage());
                            } finally {
                                final Mutex mutex = new Mutex();
                                mutex.lock();
                                decrement(name);
                                mutex.unlock();
                            }
                        }
                    });
                } else {
                    logger.error("The return-type of the method '" + method.getName() + "' must be 'void'!");
                }
            }
        }
    }

    protected void waitFor(final String name) {
        while (isRunning(name)) {
        }
    }

    private void increment(final String name) {
        if (running.containsKey(name)) {
            running.put(name, running.get(name) + 1);
        } else {
            running.put(name, 1);
        }
    }

    private void decrement(final String name) {
        if (isRunning(name)) {
            running.put(name, running.get(name) - 1);
        }
    }

    public boolean isRunning(final String name) {
        return running.containsKey(name) && running.get(name) > 0;
    }

    public boolean isReady(final String name) {
        return !isRunning(name);
    }
}
