package com.example.WorkHub.model;

public enum TaskStatus {

    P(1), // PENDING
    IP(2), // IN_PROGRESS
    C(3); // COMPLETED

    private final int order;

    TaskStatus(int order) {
        this.order = order;
    }

    /**
     * Returns true only if stauts transition is valid
     * Blocks: backwards moves, same-status no-ops, and skipping steps.
     */
    public boolean canTransitionTo(TaskStatus next) {
        return next.order == this.order + 1;
    }
}
