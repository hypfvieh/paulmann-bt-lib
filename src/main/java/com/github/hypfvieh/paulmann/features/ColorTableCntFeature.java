package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class ColorTableCntFeature extends AbstractByteValFeature {

    public ColorTableCntFeature(BluetoothGattCharacteristic _char) {
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
        return 16;
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
        return 16;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureIdent<?> getFeatureIdent() {
        return FeatureIdent.PAULMANN_COLOR_TABLE_CNT_FEATURE;
    }

}
