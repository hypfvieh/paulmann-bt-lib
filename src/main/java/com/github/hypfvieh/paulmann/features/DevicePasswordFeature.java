package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class DevicePasswordFeature extends AbstractStringValFeature {

    private boolean authenticated;

    public DevicePasswordFeature(BluetoothGattCharacteristic _char) {
        super(_char);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue() {
        return "0123";
    }

    /**
     * Authenticate at the device using the given password.
     * The same method will be used to setup a password if non is set (e.g. device was unplugged from power).
     *
     * @param _password
     * @return
     */
    public boolean authenticate(String _password) {
        authenticated = writeString(_password);
        if (authenticated) {
            return authenticated;
        }

        // password setup is a bit fuzzy
        // if device has been powered recently or does not have a password yet
        // this call has to be issued multiple times to be successful
        // we try this 5 times if call failed

        for (int i = 0; i < 5; i++) {
            if (!authenticated) {
                sleep(300L);
                authenticated = writeString(_password);
            } else {
                break;
            }
        }

        return authenticated;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * Helper method to sleep some time without try catch stuff...
     * @param _timeMs
     */
    private static void sleep(long _timeMs) {
        try {
            Thread.sleep(_timeMs); // add some delay, so we do not flood the device with requests
        } catch (InterruptedException _ex) { }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureIdent<?> getFeatureIdent() {
        return FeatureIdent.PAULMANN_DEVICE_PASSWORD_FEATURE;
    }

}
