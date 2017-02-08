package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class BluetoothBrightnessFeature extends AbstractBluetoothIntValFeature {


    public BluetoothBrightnessFeature(BluetoothGattCharacteristic _char) {
        super(_char);
        setFeatureIdent(FeatureIdent.PAULMANN_BRIGHTNESS_FEATURE);
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
        // TODO Auto-generated method stub
        return 10;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getMinValue() {
        // TODO Auto-generated method stub
        return 10;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getMaxValue() {
        // TODO Auto-generated method stub
        return 100;
    }

}
