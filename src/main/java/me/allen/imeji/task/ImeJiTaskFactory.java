package me.allen.imeji.task;

import co.aikar.taskchain.AsyncQueue;
import co.aikar.taskchain.GameInterface;
import co.aikar.taskchain.TaskChainAsyncQueue;
import co.aikar.taskchain.TaskChainFactory;
import lombok.RequiredArgsConstructor;
import me.allen.imeji.ImeJi;

import java.util.concurrent.TimeUnit;


public class ImeJiTaskFactory extends TaskChainFactory {

    public ImeJiTaskFactory(ImeJi imeJi, AsyncQueue asyncQueue) {
        super(new ImeJiInterface(imeJi, asyncQueue));
    }

    public static TaskChainFactory create() {
        return new ImeJiTaskFactory(ImeJi.getInstance(), new TaskChainAsyncQueue());
    }

    @RequiredArgsConstructor
    private static class ImeJiInterface implements GameInterface {

        private final ImeJi imeJi;
        private final AsyncQueue asyncQueue;

        @Override
        public boolean isMainThread() {
            return Thread.currentThread().equals(ImeJi.getInstance().getPrimaryThread());
        }

        @Override
        public AsyncQueue getAsyncQueue() {
            return this.asyncQueue;
        }

        @Override
        public void postToMain(Runnable run) {
            this.imeJi.getPrimaryThread().postTask(run);
        }

        @Override
        public void scheduleTask(int gameUnits, Runnable run) {
            this.imeJi.getPrimaryThread().postTask(run, gameUnits);
        }

        @Override
        public void registerShutdownHandler(TaskChainFactory factory) {
            factory.shutdown(1, TimeUnit.SECONDS);
        }

    }
}
