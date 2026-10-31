

package edu.vt.ece.hw4.locks;

import edu.vt.ece.hw4.utils.ThreadCluster;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleHLock implements Lock {
    private final int clusters;
    private final ReentrantLock[] localLocks;
    private final ReentrantLock globalLock;
    private static final int BATCH_COUNT = 5; // Define batch size for fairness
    private ThreadLocal<Integer> localCounter = ThreadLocal.withInitial(() -> 0);

    public SimpleHLock(int clusters) {
        this.clusters = clusters;
        this.localLocks = new ReentrantLock[clusters];
        for (int i = 0; i < clusters; i++) {
            localLocks[i] = new ReentrantLock();
        }
        this.globalLock = new ReentrantLock();
    }

    @Override
    public void lock() {
        int clusterId = ThreadCluster.getCluster();  // Get thread's cluster ID
        ReentrantLock localLock = localLocks[clusterId];

        localLock.lock();  // Acquire the local lock

        // If this is the first thread in the local cluster, acquire the global lock
        if (localLock.getHoldCount() == 1) {
            globalLock.lock();
        }
        localCounter.set(localCounter.get() + 1);

    }

    @Override
    public void unlock() {
        int clusterId = ThreadCluster.getCluster();
        ReentrantLock localLock = localLocks[clusterId];

        // Release local lock
        if (localCounter.get() % BATCH_COUNT == 0 || localLock.getHoldCount() == 1) {
            globalLock.unlock();  // Release global lock if no waiting threads or batch size met
        }
        localLock.unlock();  // Release the local lock
        localCounter.set(localCounter.get() - 1);
    }
}