package cn.wuxia.project.common.support;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
 * @author lisonglin
 */
public class AsyncTaskManager {

    private ScheduledThreadPoolExecutor scheduledExecutor;
    private ExecutorService fixedExecutor;
    private ExecutorService singleExecutor;


    public AsyncTaskManager(final String threadName) {
        int threadSize = Runtime.getRuntime().availableProcessors() * 2;
        scheduledExecutor = new ScheduledThreadPoolExecutor(threadSize,
                new BasicThreadFactory.Builder().namingPattern(threadName + "-pool-%d").daemon(true).build());
        fixedExecutor = new ThreadPoolExecutor(threadSize, threadSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new BasicThreadFactory.Builder().namingPattern(threadName + "-pool-%d").daemon(true).build());
        singleExecutor = Executors.newSingleThreadExecutor(
                new BasicThreadFactory.Builder().namingPattern(threadName + "-pool-%d").daemon(true).build());

    }

    public static AsyncTaskManager build(final String threadName) {
        return new AsyncTaskManager(threadName);
    }

    public ScheduledThreadPoolExecutor getScheduledExecutor() {
        return scheduledExecutor;
    }

    public ExecutorService getFixedExecutor() {
        return fixedExecutor;
    }

    public ExecutorService getSingleExecutor() {
        return singleExecutor;
    }


    public void shutdown() {
        if (scheduledExecutor != null && !scheduledExecutor.isShutdown()) {
            scheduledExecutor.shutdown();
        }
        if (fixedExecutor != null && !fixedExecutor.isShutdown()) {
            fixedExecutor.shutdown();
        }
        if (singleExecutor != null && !singleExecutor.isShutdown()) {
            singleExecutor.shutdown();
        }

    }

    public void shutdownNow() {
        if (scheduledExecutor != null && !scheduledExecutor.isShutdown()) {
            scheduledExecutor.shutdownNow();
        }
        if (fixedExecutor != null && !fixedExecutor.isShutdown()) {
            fixedExecutor.shutdownNow();
        }
        if (singleExecutor != null && !singleExecutor.isShutdown()) {
            singleExecutor.shutdownNow();
        }
    }
}
