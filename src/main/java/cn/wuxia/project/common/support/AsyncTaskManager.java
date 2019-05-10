package cn.wuxia.project.common.support;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

public class AsyncTaskManager {

    private ExecutorService executor;

//    private int count;


    public AsyncTaskManager(final String threadName) {
        executor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2,
                new BasicThreadFactory.Builder().namingPattern(threadName + "-pool-%d").daemon(true).build());
//
//        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2, new ThreadFactory() {
//            @Override
//            public Thread newThread(Runnable r) {
//                Thread t = new Thread(r);
//                t.setName(threadName + (++count));
//                return t;
//            }
//        });
    }

    public static AsyncTaskManager build(final String threadName) {
        return new AsyncTaskManager(threadName);
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void shutdown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    public void shutdownNow() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
    }
}
