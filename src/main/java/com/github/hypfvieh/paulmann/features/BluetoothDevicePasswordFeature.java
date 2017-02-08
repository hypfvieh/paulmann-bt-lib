package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class BluetoothDevicePasswordFeature extends AbstractBluetoothStringValFeature {

    private boolean authenticated;

    public BluetoothDevicePasswordFeature(BluetoothGattCharacteristic _char) {
        super(_char);
        setFeatureIdent(FeatureIdent.PAULMANN_ON_OFF_FEATURE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue() {
        return null;
    }

    public boolean authenticate(String _password) {
        authenticated = writeString(_password);
        return authenticated;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

}
