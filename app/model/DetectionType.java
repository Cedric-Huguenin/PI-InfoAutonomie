package model;

/**
 * Detection types
 * Created by Ced on 23/02/2015.
 */
public enum DetectionType {
    /**
     * Difference w=between consecutive values.
     */
    DELTA,
    /**
     * Value between a man and a min.
     */
    MIN_MAX_VALUES,
    /**
     * Simple threshold.
     */
    SIMPLE_THRESHOLD
}
