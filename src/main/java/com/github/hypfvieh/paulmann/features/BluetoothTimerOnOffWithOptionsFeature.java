package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class BluetoothTimerOnOffWithOptionsFeature extends AbstractBluetoothIntValFeature {

    public static final int TIMEROPTION_FADE_OFF = 0;
    public static final int TIMEROPTION_FADE_1SEC = 1;
    public static final int TIMEROPTION_FADE_1MIN = 2;
    public static final int TIMEROPTION_FADE_10MIN = 3;

    public BluetoothTimerOnOffWithOptionsFeature(BluetoothGattCharacteristic _char) {
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
        return 3;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureIdent<?> getFeatureIdent() {
        return FeatureIdent.PAULMANN_ON_OFF_TIMER_WITH_OPTIONS_FEATURE;
    }

}
