package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class BluetoothOnOffFeature extends AbstractBluetoothIntValFeature {

    public BluetoothOnOffFeature(BluetoothGattCharacteristic _char) {
        super(_char);
    }

    public boolean toggle(boolean _turnOn) {
        return writeByte(_turnOn ? getMaxValue() : getMinValue());
    }

    public boolean isTurnedOn() {
        return readByte() == getMaxValue() ? true : false;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureIdent<?> getFeatureIdent() {
        return FeatureIdent.PAULMANN_ON_OFF_FEATURE;
    }

}
