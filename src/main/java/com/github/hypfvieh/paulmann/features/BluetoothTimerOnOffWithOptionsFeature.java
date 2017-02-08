package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class BluetoothTimerOnOffWithOptionsFeature extends AbstractBluetoothIntValFeature {

    public BluetoothTimerOnOffWithOptionsFeature(BluetoothGattCharacteristic _char) {
        super(_char);
        setFeatureIdent(FeatureIdent.PAULMANN_ON_OFF_TIMER_WITH_OPTIONS_FEATURE);
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
