package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class ColorTemperatureDigitalRGBFeature extends AbstractPredefinedIntValFeature {

    protected ColorTemperatureDigitalRGBFeature(BluetoothGattCharacteristic _characteristic) {
        super(_characteristic);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void populatePredefinedList() {
        addPredefinedValue(new PredefinedValue("Color Temp 2700K", 2700));
        addPredefinedValue(new PredefinedValue("Color Temp 4000K", 4000));
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
