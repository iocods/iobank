package iocode.web.app.entity;

/**
 * Represents the status of a task or operation.
 *
 * This enum is used to indicate the current state of a task or operation.
 * The possible values are:
 * <ul>
 *     <li>{@link #PENDING}: The task or operation is waiting to be processed.</li>
 *     <li>{@link #COMPLETED}: The task or operation has been successfully completed.</li>
 *     <li>{@link #FAILED}: The task or operation has failed.</li>
 * </ul>
 */
public enum Status {
    PENDING,
    COMPLETED,
    FAILED
}
