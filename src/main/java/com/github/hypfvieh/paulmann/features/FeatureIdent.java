package com.github.hypfvieh.paulmann.features;

import static com.github.hypfvieh.paulmann.features.FeatureMode.READ;
import static com.github.hypfvieh.paulmann.features.FeatureMode.READWRITE;
import static com.github.hypfvieh.paulmann.features.FeatureMode.WRITE;

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
public final class FeatureIdent<T extends AbstractFeature> {

    public static final String PAULMANN_SERVICE_ID = "FFB0";

    public static final List<FeatureIdent<?>> ALL_FEATURES = new ArrayList<>();

    // only for RGB/RGBW and DigitalRGB
    public static final FeatureIdent<AutoChangeControlFeature> PAULMANN_AUTOCHANGE_CONTROL_FEATURE = new FeatureIdent<>(
            PAULMANN_SERVICE_ID, "FFB1", AutoChangeControlFeature.class, "PAULMANN_AUTOCHANGE_CONTROL_FEATURE", READWRITE);
    public static final FeatureIdent<RgbFeature> PAULMANN_RGB_FEATURE = new FeatureIdent<>(PAULMANN_SERVICE_ID, "FFB2",
            RgbFeature.class, "PAULMANN_RGB_FEATURE", READWRITE);

    // all devices
    public static final FeatureIdent<SystemTimeFeature> PAULMANN_SYSTEMTIME_FEATURE = new FeatureIdent<>(
            PAULMANN_SERVICE_ID, "FFB3", SystemTimeFeature.class, "PAULMANN_SYSTEMTIME_FEATURE", READWRITE);
    public static final FeatureIdent<TimerFeature> PAULMANN_TIMER_FEATURE = new FeatureIdent<>(
            PAULMANN_SERVICE_ID, "FFB4", TimerFeature.class, "PAULMANN_TIMER_FEATURE", READWRITE);

    public static final FeatureIdent<DeviceNameFeature> PAULMANN_DEVICENAME_FEATURE = new FeatureIdent<>(
            PAULMANN_SERVICE_ID, "FFB5", DeviceNameFeature.class, "PAULMANN_DEVICENAME_FEATURE", READWRITE);

    // Color temp feature for RGBW Lamps
    public static final FeatureIdent<ColorTemperatureRGBWFeature> PAULMANN_COLORTEMP_RGBW_FEATURE = new FeatureIdent<>(
            PAULMANN_SERVICE_ID, "FFB6", ColorTemperatureRGBWFeature.class, "PAULMANN_COLORTEMP_RGBW_FEATURE", READWRITE);
    // Color temp feature for WC-Lamps
    public static final FeatureIdent<ColorTemperatureRGBWFeature> PAULMANN_COLORTEMP_WCLAMP_FEATURE = new FeatureIdent<>(
            PAULMANN_SERVICE_ID, "FFB6", ColorTemperatureRGBWFeature.class, "PAULMANN_COLORTEMP_WCLAMP_FEATURE", READWRITE);
    // Color temp feature for DigitalRGB Lamps
    public static final FeatureIdent<ColorTemperatureRGBWFeature> PAULMANN_COLORTEMP_DIGITALRGB_FEATURE = new FeatureIdent<>(
            PAULMANN_SERVICE_ID, "FFB6", ColorTemperatureRGBWFeature.class, "PAULMANN_COLORTEMP_DIGITALRGB_FEATURE", READWRITE);

    public static final FeatureIdent<OnOffFeature> PAULMANN_ON_OFF_FEATURE = new FeatureIdent<>(PAULMANN_SERVICE_ID, "FFB7",
            OnOffFeature.class, "PAULMANN_ON_OFF_FEATURE", READWRITE);

    public static final FeatureIdent<BrightnessFeature> PAULMANN_BRIGHTNESS_FEATURE = new FeatureIdent<>(
            PAULMANN_SERVICE_ID, "FFB8", BrightnessFeature.class, "PAULMANN_BRIGHTNESS_FEATURE", READWRITE);
    public static final FeatureIdent<?> PAULMANN_WORKINGMODE_FEATURE = new FeatureIdent<>(PAULMANN_SERVICE_ID, "FFB9", null,
            "PAULMANN_WORKINGMODE_FEATURE", READ);

    public static final FeatureIdent<DevicePasswordFeature> PAULMANN_DEVICE_PASSWORD_FEATURE = new FeatureIdent<>(
            PAULMANN_SERVICE_ID, "FFBA", DevicePasswordFeature.class, "PAULMANN_DEVICE_PASSWORD_FEATURE", WRITE);
    public static final FeatureIdent<RemoteControlModeFeature> PAULMANN_REMOTECONTROLMODE_FEATURE = new FeatureIdent<>(
            PAULMANN_SERVICE_ID, "FFBB", RemoteControlModeFeature.class, "PAULMANN_REMOTECONTROLMODE_FEATURE", READWRITE);

    // DigitalRGB only
    public static final FeatureIdent<IcCountFeature> PAULMANN_IC_COUNT_FEATURE = new FeatureIdent<>(
            PAULMANN_SERVICE_ID, "FFBC", IcCountFeature.class, "PAULMANN_IC_COUNT_FEATURE", READWRITE);
    public static final FeatureIdent<ColorTableFeature> PAULMANN_COLOR_TABLE_FEATURE = new FeatureIdent<>(
            PAULMANN_SERVICE_ID, "FFBC", ColorTableFeature.class, "PAULMANN_COLOR_TABLE_FEATURE", READWRITE);
    public static final FeatureIdent<ColorTableIdxFeature> PAULMANN_COLOR_TABLE_IDX_FEATURE = new FeatureIdent<>(
            PAULMANN_SERVICE_ID, "FFBC", ColorTableIdxFeature.class, "PAULMANN_COLOR_TABLE_IDX_FEATURE", READWRITE);
    public static final FeatureIdent<ColorTableCntFeature> PAULMANN_COLOR_TABLE_CNT_FEATURE = new FeatureIdent<>(
            PAULMANN_SERVICE_ID, "FFBC", ColorTableCntFeature.class, "PAULMANN_COLOR_TABLE_CNT_FEATURE", READWRITE);


    public static final String UUID_BASE = "0000XXXX-0000-1000-8000-00805f9b34fb";

    private final String serviceId;
    private final String charId;
    private final Class<T> deviceClass;
    private final String name;
    private final FeatureMode mode;

    private FeatureIdent(String _serviceId, String _charId, Class<T> _deviceClass, String _name, FeatureMode _mode) {
        serviceId = createUUID(_serviceId);
        charId = createUUID(_charId);
        deviceClass = _deviceClass;
        name = _name;
        mode = _mode;
        ALL_FEATURES.add(this);
    }

    public String getName() {
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

    public FeatureMode getMode() {
        return mode;
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
        result = prime * result + ((mode == null) ? 0 : mode.hashCode());
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
        if (mode != other.mode) {
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
