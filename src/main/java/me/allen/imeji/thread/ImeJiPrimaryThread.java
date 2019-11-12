package me.allen.imeji.thread;

import me.allen.imeji.task.type.ImeJiTask;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ImeJiPrimaryThread extends Thread {

    private ConcurrentLinkedQueue<ImeJiTask> taskQueue;
    private Timer timer = new Timer();

    public ImeJiPrimaryThread() {
        super("ImeJi | Primary");
        this.taskQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void run() {
        while (!this.taskQueue.isEmpty()) {
            ImeJiTask imeJiTask = this.taskQueue.poll();
            if (imeJiTask.getTicks() > 0) {
                this.timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        imeJiTask.getTask().run();
                    }
                }, imeJiTask.getTicks());
            } else {
                imeJiTask.getTask().run();
            }
        }
    }

    public void postTask(Runnable runnable) {
        this.postTask(runnable, 0);
    }

    public void postTask(Runnable runnable, int ticks) {
        this.taskQueue.add(new ImeJiTask(ticks, runnable));
    }

}
