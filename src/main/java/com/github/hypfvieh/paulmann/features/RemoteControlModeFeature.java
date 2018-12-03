package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class RemoteControlModeFeature extends AbstractByteValFeature {

    public RemoteControlModeFeature(BluetoothGattCharacteristic _char) {
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
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureIdent<?> getFeatureIdent() {
        return FeatureIdent.PAULMANN_REMOTECONTROLMODE_FEATURE;
    }

}
