package com.github.hypfvieh.paulmann.features;

import java.util.ArrayList;
import java.util.List;

import com.github.hypfvieh.paulmann.devices.AbstractPaulmannDevice;

/**
 * Allow known features of Paulmann devices.
 * This includes the UUID of the GATT service and characteristics.
 * All enum values also provide the class which implements the feature behind the enum value.
 *
 * Sadly using enum would prevent deny the proper usage of generics in {@link AbstractPaulmannDevice},
 * so the 'poor-mans' enum is used.
 *
 * @author David M.
 *
 */
public final class FeatureIdent<T extends AbstractBluetoothFeature> {

    public static final List<FeatureIdent<?>> ALL_FEATURES = new ArrayList<>();

    public static final FeatureIdent<BluetoothAutomotiveFeature> PAULMANN_AUTOMOTIVE_INTERVAL_FEATURE = new FeatureIdent<>(
            "FFB0", "FFB1", BluetoothAutomotiveFeature.class, "PAULMANN_AUTOMOTIVE_INTERVAL_FEATURE");
    public static final FeatureIdent<BluetoothRgbFeature> PAULMANN_RGB_FEATURE = new FeatureIdent<>("FFB0", "FFB2",
            BluetoothRgbFeature.class, "PAULMANN_RGB_FEATURE");
    public static final FeatureIdent<BluetoothSystemTimeFeature> PAULMANN_SYSTEMTIME_FEATURE = new FeatureIdent<>(
            "FFB0", "FFB3", BluetoothSystemTimeFeature.class, "PAULMANN_SYSTEMTIME_FEATURE");
    public static final FeatureIdent<BluetoothTimerOnOffWithOptionsFeature> PAULMANN_ON_OFF_TIMER_WITH_OPTIONS_FEATURE = new FeatureIdent<>(
            "FFB0", "FFB4", BluetoothTimerOnOffWithOptionsFeature.class, "PAULMANN_ON_OFF_TIMER_WITH_OPTIONS_FEATURE");
    public static final FeatureIdent<BluetoothColorTemperatureFeature> PAULMANN_COLORTEMP_FEATURE = new FeatureIdent<>(
            "FFB0", "FFB6", BluetoothColorTemperatureFeature.class, "PAULMANN_COLORTEMP_FEATURE");
    public static final FeatureIdent<BluetoothOnOffFeature> PAULMANN_ON_OFF_FEATURE = new FeatureIdent<>("FFB0", "FFB7",
            BluetoothOnOffFeature.class, "PAULMANN_ON_OFF_FEATURE");
    public static final FeatureIdent<BluetoothBrightnessFeature> PAULMANN_BRIGHTNESS_FEATURE = new FeatureIdent<>(
            "FFB0", "FFB8", BluetoothBrightnessFeature.class, "PAULMANN_BRIGHTNESS_FEATURE");
    public static final FeatureIdent<?> PAULMANN_WORKINGMODE_FEATURE = new FeatureIdent<>("FFB0", "FFB9", null,
            "PAULMANN_WORKINGMODE_FEATURE");
    public static final FeatureIdent<BluetoothDevicePasswordFeature> PAULMANN_DEVICE_PASSWORD_FEATURE = new FeatureIdent<>(
            "FFB0", "FFBA", BluetoothDevicePasswordFeature.class, "PAULMANN_DEVICE_PASSWORD_FEATURE");
    public static final FeatureIdent<BluetoothRemoteControlModeFeature> PAULMANN_REMOTECONTROLMODE_FEATURE = new FeatureIdent<>(
            "FFB0", "FFBB", BluetoothRemoteControlModeFeature.class, "PAULMANN_REMOTECONTROLMODE_FEATURE");

    public static final String UUID_BASE = "0000XXXX-0000-1000-8000-00805f9b34fb";

    private final String serviceId;
    private final String charId;
    private final Class<T> deviceClass;
    private final String name;

    private FeatureIdent(String _serviceId, String _charId, Class<T> _deviceClass, String _name) {
        serviceId = createUUID(_serviceId);
        charId = createUUID(_charId);
        deviceClass = _deviceClass;
        name = _name;
        ALL_FEATURES.add(this);
    }

    public String name() {
        return name;
    }

    private static String createUUID(String _uuidPart) {
        return UUID_BASE.replace("XXXX", _uuidPart);
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getCharId() {
        return charId;
    }

    public Class<T> getDeviceClass() {
        return deviceClass;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((charId == null) ? 0 : charId.hashCode());
        result = prime * result + ((deviceClass == null) ? 0 : deviceClass.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((serviceId == null) ? 0 : serviceId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FeatureIdent<?> other = (FeatureIdent<?>) obj;
        if (charId == null) {
            if (other.charId != null) {
                return false;
            }
        } else if (!charId.equals(other.charId)) {
            return false;
        }
        if (deviceClass == null) {
            if (other.deviceClass != null) {
                return false;
            }
        } else if (!deviceClass.equals(other.deviceClass)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (serviceId == null) {
            if (other.serviceId != null) {
                return false;
            }
        } else if (!serviceId.equals(other.serviceId)) {
            return false;
        }
        return true;
    }

}
