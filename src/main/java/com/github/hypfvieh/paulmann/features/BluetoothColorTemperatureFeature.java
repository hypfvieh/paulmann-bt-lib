package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class BluetoothColorTemperatureFeature extends AbstractBluetoothPredefinedIntValFeature {

    protected BluetoothColorTemperatureFeature(BluetoothGattCharacteristic _characteristic) {
        super(_characteristic);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void populatePredefinedList() {
        addPredefinedValue(new PredefinedValue("Color Temp. 1", 2700));
        addPredefinedValue(new PredefinedValue("Color Temp. 2", 3000));
        addPredefinedValue(new PredefinedValue("Color Temp. 3", 3500));
        addPredefinedValue(new PredefinedValue("Color Temp. 4", 4000));
        addPredefinedValue(new PredefinedValue("Color Temp. 5", 4500));
        addPredefinedValue(new PredefinedValue("Color Temp. 6", 5000));
        addPredefinedValue(new PredefinedValue("Color Temp. 7", 6500));
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
        return FeatureIdent.PAULMANN_COLORTEMP_FEATURE;
    }

}
