package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class ColorTemperatureWCLampFeature extends AbstractIntValFeature {

    protected ColorTemperatureWCLampFeature(BluetoothGattCharacteristic _characteristic) {
        super(_characteristic);
    }

    @Override
    public int getStepSize() {
        return 50;
    }

    @Override
    public int getDefaultValue() {
        return 2700;
    }

    @Override
    public int getMinValue() {
        return 2700;
    }

    @Override
    public int getMaxValue() {
        return 6500;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureIdent<?> getFeatureIdent() {
        return FeatureIdent.PAULMANN_COLORTEMP_WCLAMP_FEATURE;
    }

}
