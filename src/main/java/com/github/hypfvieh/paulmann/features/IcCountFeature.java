package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class IcCountFeature extends AbstractByteValFeature {

    public IcCountFeature(BluetoothGattCharacteristic _char) {
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
        return 50;
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
        return 50;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureIdent<?> getFeatureIdent() {
        return FeatureIdent.PAULMANN_IC_COUNT_FEATURE;
    }

}
