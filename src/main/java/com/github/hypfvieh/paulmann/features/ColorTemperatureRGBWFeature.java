package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class ColorTemperatureRGBWFeature extends AbstractPredefinedIntValFeature {

    protected ColorTemperatureRGBWFeature(BluetoothGattCharacteristic _characteristic) {
        super(_characteristic);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void populatePredefinedList() {
        addPredefinedValue(new PredefinedValue("Color Temp 2400K", 2400));
        addPredefinedValue(new PredefinedValue("Color Temp 2700K", 2700));
        addPredefinedValue(new PredefinedValue("Color Temp 3000K", 3000));
        addPredefinedValue(new PredefinedValue("Color Temp 4000K", 4000));
        addPredefinedValue(new PredefinedValue("Color Temp 5000K", 5000));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDefaultValue() {
        return 3000;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureIdent<?> getFeatureIdent() {
        return FeatureIdent.PAULMANN_COLORTEMP_RGBW_FEATURE;
    }

}
