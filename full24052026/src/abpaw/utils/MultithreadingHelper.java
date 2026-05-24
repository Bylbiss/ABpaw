/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.utils;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class MultithreadingHelper {
    
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void runInBackground(Runnable task) {
        executor.submit(task);
    }
 
    public static <T> void runWithCallback(CallbackTask<T> backgroundTask, Consumer<T> onSuccess) {
        executor.submit(() -> {
            T result = backgroundTask.execute();
            SwingUtilities.invokeLater(() -> onSuccess.accept(result));
        });
    }

    public static Thread startPeriodicTask(Runnable task, long intervalMillis) {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    task.run();
                    Thread.sleep(intervalMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        return thread;
    }
    
    public static void stopPeriodicTask(Thread taskThread) {
        if (taskThread != null && taskThread.isAlive()) {
            taskThread.interrupt();
        }
    }
 
    public static void shutdown() {
        executor.shutdown();
    }
  
    @FunctionalInterface
    public interface CallbackTask<T> {
        T execute();
    }
}