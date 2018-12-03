package com.github.hypfvieh.paulmann.features;

/**
 * Specifies if a feature can be read or write or both.
 */
public enum FeatureMode {
    READ,
    WRITE,
    READWRITE;

    public static boolean canWrite(FeatureMode _mode) {
        return _mode != null && _mode == READWRITE || _mode == WRITE;
    }

    public static boolean canRead(FeatureMode _mode) {
        return _mode != null && _mode == READWRITE || _mode == READ;
    }

}
