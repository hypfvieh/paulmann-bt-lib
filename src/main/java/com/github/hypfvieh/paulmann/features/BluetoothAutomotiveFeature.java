package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class BluetoothAutomotiveFeature extends AbstractBluetoothIntValFeature {

    public BluetoothAutomotiveFeature(BluetoothGattCharacteristic _char) {
        super(_char);
        setFeatureIdent(FeatureIdent.PAULMANN_AUTOMOTIVE_INTERVAL_FEATURE);
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

}
