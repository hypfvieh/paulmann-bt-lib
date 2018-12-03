package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class BrightnessFeature extends AbstractByteValFeature {

    public BrightnessFeature(BluetoothGattCharacteristic _char) {
        super(_char);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getStepSize() {
        return 5;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getDefaultValue() {
        return 10;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getMinValue() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getMaxValue() {
        return 100;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureIdent<?> getFeatureIdent() {
        return FeatureIdent.PAULMANN_BRIGHTNESS_FEATURE;
    }

}
