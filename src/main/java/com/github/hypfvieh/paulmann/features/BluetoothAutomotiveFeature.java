package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class BluetoothAutomotiveFeature extends AbstractBluetoothIntValFeature {

    public static final int AUTOMOTIVE_MODE_RGB = -1;
    public static final int AUTOMOTIVE_MODE_FLASH = 0;
    public static final int AUTOMOTIVE_MODE_STROBEFADE = 1;
    public static final int AUTOMOTIVE_MODE_FADE = 2;
    public static final int AUTOMOTIVE_MODE_SMOOTH = 3;

    public BluetoothAutomotiveFeature(BluetoothGattCharacteristic _char) {
        super(_char);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getStepSize() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getDefaultValue() {
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getMinValue() {
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getMaxValue() {
        return 3;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureIdent<?> getFeatureIdent() {
        return FeatureIdent.PAULMANN_AUTOMOTIVE_INTERVAL_FEATURE;
    }

}
