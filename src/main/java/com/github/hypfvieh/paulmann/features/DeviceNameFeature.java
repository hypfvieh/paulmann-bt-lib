package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class DeviceNameFeature extends AbstractStringValFeature {

    public DeviceNameFeature(BluetoothGattCharacteristic _char) {
        super(_char);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue() {
        return null;
    }

   /**
     * {@inheritDoc}
     */
    @Override
    public FeatureIdent<?> getFeatureIdent() {
        return FeatureIdent.PAULMANN_DEVICENAME_FEATURE;
    }

}
