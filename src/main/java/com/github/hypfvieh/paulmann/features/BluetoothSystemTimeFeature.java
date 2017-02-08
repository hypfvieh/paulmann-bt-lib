package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class BluetoothSystemTimeFeature extends AbstractBluetoothIntValFeature {

    public BluetoothSystemTimeFeature(BluetoothGattCharacteristic _char) {
        super(_char);
        setFeatureIdent(FeatureIdent.PAULMANN_SYSTEMTIME_FEATURE);
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
        return 0;
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
        return 1;
    }

}
