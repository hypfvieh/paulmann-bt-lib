package com.github.hypfvieh.paulmann.devices;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattService;
import com.github.hypfvieh.paulmann.features.FeatureIdent;

/**
 * Concrete PaulmannDevice which describes a RGB-W (RGB plus white) LED stripe.
 *
 * @author David M.
 */
public class LampRGBW extends AbstractPaulmannDevice {
    public static final FeatureIdent<?>[] SUPPORTED_FEATURES = {
            FeatureIdent.PAULMANN_RGB_FEATURE,
            FeatureIdent.PAULMANN_ON_OFF_FEATURE,
            FeatureIdent.PAULMANN_BRIGHTNESS_FEATURE,
            FeatureIdent.PAULMANN_COLORTEMP_RGBW_FEATURE,
            FeatureIdent.PAULMANN_AUTOCHANGE_CONTROL_FEATURE,
            FeatureIdent.PAULMANN_TIMER_FEATURE,
            FeatureIdent.PAULMANN_SYSTEMTIME_FEATURE,
            FeatureIdent.PAULMANN_WORKINGMODE_FEATURE,
            FeatureIdent.PAULMANN_REMOTECONTROLMODE_FEATURE,
            FeatureIdent.PAULMANN_DEVICE_PASSWORD_FEATURE
    };

    public static final String DEVICE_ALIAS = "Lamp-RGBW";

    public LampRGBW(BluetoothGattService _gattService) {
        super(_gattService, SUPPORTED_FEATURES);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureIdent<?>[] getAllSupportedFeatures() {
        return SUPPORTED_FEATURES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDeviceTypeID() {
        return 0xD1;
    }

}
