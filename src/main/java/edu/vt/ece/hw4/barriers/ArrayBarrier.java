package edu.vt.ece.hw4.barriers;

public class ArrayBarrier implements Barrier{
    private final int n;
    private final int[] b;

    public ArrayBarrier(int n) {
        this.n = n;
        this.b = new int[n];
        b[0] = 1;
    }
    @Override
    public void enter() {
        int id = (int) Thread.currentThread().getId() % n;

        if (id == 0) {
            b[id] = 1;
        } else {
            while (b[id - 1] == 0) {
                // Spin-wait until previous thread sets its position
            }
            b[id] = 1;
        }

        if (id == n - 1) {
            b[id] = 2;  // Last thread sets final position to allow others to proceed
        } else {
            while (b[n - 1] != 2) {
                // Spin-wait until the last thread has left the barrier
            }
        }

    }
}
