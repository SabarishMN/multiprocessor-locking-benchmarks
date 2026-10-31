package edu.vt.ece.hw4;

import edu.vt.ece.hw4.barriers.ArrayBarrier;
import edu.vt.ece.hw4.barriers.Barrier;
import edu.vt.ece.hw4.barriers.TTASBarrier;
import edu.vt.ece.hw4.bench.*;
import edu.vt.ece.hw4.locks.*;
import edu.vt.ece.hw4.utils.ThreadCluster;


public class Benchmark {

    private static final String ALOCK = "ALock";
    private static final String BACKOFFLOCK = "BackoffLock";
    private static final String MCSLOCK = "MCSLock";
    private static final String TTASLOCK = "TTASLock";
    private static final String ARRAYLOCK = "ArrayLock";
    private static final String SPINSLEEPLOCK= "SpinSleepLock";
    private static final String SIMPLEHLOCK = "SimpleHLock";
    private static final String PRIORITYLOCK = "PriorityQueueLock";
    private static final String CLHLOCK = "CLHLock";

    public static void main(String[] args) throws Exception {
        String mode = args.length <= 0 ? "normal" : args[0];
        String lockClass = (args.length <= 1 ? PRIORITYLOCK : args[1]);
        int threadCount = (args.length <= 2 ? 8 : Integer.parseInt(args[2]));
        int totalIters = (args.length <= 3 ? 64000 : Integer.parseInt(args[3]));
        int clusters = args.length <= 4 ? 6 : Integer.parseInt(args[4]);
        int iters = totalIters / threadCount;

        run(args, mode, lockClass, threadCount, iters, clusters);

    }

    private static void run(String[] args, String mode, String lockClass, int threadCount, int iters, int clusters) throws Exception {
        for (int i = 0; i < 5; i++) {
            Lock lock = null;
            switch (lockClass.trim()) {
                case ALOCK:
                    lock = new ALock(threadCount);
                    break;
                case BACKOFFLOCK: // Exponential, Fibonacci, Fixed, Linear, Polynomial
                    lock = new BackoffLock("Polynomial");
                    break;
                case MCSLOCK:
                    lock = new MCSLock();
                    break;
                    case TTASLOCK:
                        lock = new TTASLock();
                        break;
                        case ARRAYLOCK:
                            lock = new ALock(threadCount);
                            break;
                            case SPINSLEEPLOCK:
                                lock = new SpinSleepLock(2);
                                break;
                case PRIORITYLOCK:
                    lock = new PriorityQueueLock(threadCount);
                    break;
                case CLHLOCK:
                    lock = new CLHLock();
                    break;

            }

            switch (mode.trim().toLowerCase()) {
                case "normal":
                    final Counter counter = new SharedCounter(0, lock);
                    runNormal(counter, threadCount, iters);
                    break;
                case "empty":
                    runEmptyCS(lock, threadCount, iters);
                    break;
                case "long":
                    runLongCS(lock, threadCount, iters);
                    break;
                case "barrier":
                    Barrier b = new TTASBarrier(threadCount);
                    Barrier a = new ArrayBarrier(threadCount);
//                    throw new UnsupportedOperationException("Complete this.");
                    runBarrier(threadCount,b);
                    Thread.sleep(2000);
                    System.out.println("\n ArrayBarrier Readings : \n");
                    runBarrier(threadCount, a);
                case "cluster":
                    lock = new SimpleHLock(clusters);
                    runClusterCS(lock, threadCount, iters, clusters);
                    break;
                default:
//                    throw new UnsupportedOperationException("Implement this");
                    return;
            }
        }
    }

    private static void runNormal(Counter counter, int threadCount, int iters) throws Exception {
        final TestThread[] threads = new TestThread[threadCount];
        TestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new TestThread(counter, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }

    private static void runEmptyCS(Lock lock, int threadCount, int iters) throws Exception {

        final EmptyCSTestThread[] threads = new EmptyCSTestThread[threadCount];
        EmptyCSTestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new EmptyCSTestThread(lock, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }

    static void runLongCS(Lock lock, int threadCount, int iters) throws Exception {
        final Counter counter = new Counter(0);
        final LongCSTestThread[] threads = new LongCSTestThread[threadCount];
        LongCSTestThread.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new LongCSTestThread(lock, counter, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread is " + totalTime / threadCount + "ms");
    }

    private static void runBarrier(int threadCount, Barrier barrier) throws InterruptedException {
        Thread[] threads = new Thread[threadCount];
        long startTime = System.nanoTime();

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                try {
                    foo();
                    barrier.enter();
                    bar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }

        long endTime = System.nanoTime();
        System.out.println("Barrier time for " + threadCount + " threads: " + (endTime - startTime) / 1_000_000 + " ms");
    }

    private static void foo() {
        System.out.println("Thread name : "+Thread.currentThread().getName());
    }

    private static void bar() {
        System.out.println("Thread name : "+Thread.currentThread().getName());
    }

    public static void runClusterCS(Lock lock, int threadCount, int iters, int clusters) throws Exception {

        SimpleHLock simpleHLock = new SimpleHLock(clusters);
        final Counter counter = new Counter(0);
        final LongCSTestThread[] threads = new LongCSTestThread[threadCount];

        ThreadCluster.reset();

        for (int t = 0; t < threadCount; t++) {
            threads[t] = new LongCSTestThread(simpleHLock, counter, iters);
        }

        for (int t = 0; t < threadCount; t++) {
            threads[t].start();
        }

        long totalTime = 0;
        for (int t = 0; t < threadCount; t++) {
            threads[t].join();
            totalTime += threads[t].getElapsedTime();
        }

        System.out.println("Average time per thread in Cluster CS is " + totalTime / threadCount + "ms");
    }

}

