package com.github.hypfvieh.paulmann.devices;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothDevice;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattService;
import com.github.hypfvieh.paulmann.features.AbstractBluetoothFeature;
import com.github.hypfvieh.paulmann.features.BluetoothFeatureFactory;
import com.github.hypfvieh.paulmann.features.FeatureIdent;

/**
 * Base class of all Paulmann devices.
 *
 * @author David M.
 *
 */
public abstract class AbstractPaulmannDevice {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Map<FeatureIdent<? extends AbstractBluetoothFeature>, AbstractBluetoothFeature> configuredFeatures = new LinkedHashMap<>();

    private final BluetoothDevice device;
    private final BluetoothGattService gattService;

    protected AbstractPaulmannDevice(BluetoothGattService _gattService) {
        device = _gattService.getDevice();
        gattService = _gattService;
    }

    protected AbstractPaulmannDevice(BluetoothGattService _gattService, FeatureIdent<?>... _featureIdent) {
        this(_gattService);
        for (FeatureIdent<?> featureIdent : _featureIdent) {
            AbstractBluetoothFeature feature = BluetoothFeatureFactory.getInstance().createFeature(featureIdent,
                    _gattService);
            if (feature == null) {
                getLogger().warn("Cannot enable supported feature {}, unable to create instance", featureIdent.name());
            } else {
                configuredFeatures.put(featureIdent, feature);
            }
        }
    }

    /**
     * Returns a unmodifiable Map of all configured subDevices/features.
     * @return unmodifiable map of {@link FeatureIdent} / {@link AbstractBluetoothFeature}
     */
    public Map<FeatureIdent<?>, AbstractBluetoothFeature> getConfiguredFeatures() {
        return Collections.unmodifiableMap(configuredFeatures);
    }

    /**
     * Get feature by given {@link FeatureIdent};
     * @param _ident {@link FeatureIdent} to retrieve
     * @param <T> some class extending {@link AbstractBluetoothFeature}
     * @return {@link AbstractBluetoothFeature} compatible object
     */
    public <T extends AbstractBluetoothFeature> T getFeature(FeatureIdent<T> _ident) {
        if (configuredFeatures == null || configuredFeatures.isEmpty()) {
            return null;
        }
        AbstractBluetoothFeature abstractBluetoothFeature = configuredFeatures.get(_ident);

        if (abstractBluetoothFeature.getClass().isAssignableFrom(_ident.getDeviceClass())) {
            return _ident.getDeviceClass().cast(abstractBluetoothFeature);
        }

        return null;
    }

    /**
     * Returns the bluetooth raw-device behind this instance.
     * @return device
     */
    public BluetoothDevice getDevice() {
        return device;
    }

    /**
     * Returns the GATT service of this instance.
     * @return gatt service
     */
    public BluetoothGattService getGattService() {
        return gattService;
    }

    /**
     * Logger for all derived classes.
     * @return logger
     */
    protected Logger getLogger() {
        return logger;
    }

    /**
     * Returns an array of supported features of the concrete class.
     *
     * @return array, never null
     */
    public abstract FeatureIdent<?>[] getAllSupportedFeatures();

    /**
     * Returns the mapping Id of the concrete class.
     * @return int
     */
    public abstract int getMappingId();

    /**
     * Checks if the given feature UUID is supported by the concrete device class.
     * @param _uuid feature-uuid to check
     * @return true if supported, false otherwise
     */
    public boolean isFeatureSupported(String _uuid) {
        if (_uuid == null) {
            return false;
        }
        for (FeatureIdent<?> featureIdent : getAllSupportedFeatures()) {
            if (featureIdent.equals(_uuid)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if the given feature is supported by the concrete class.
     * @param _ident feature to check
     * @return true if feature is supported, false otherwise
     */
    public boolean isFeatureSupported(FeatureIdent<?> _ident) {
        if (_ident == null || getAllSupportedFeatures() == null) {
            return false;
        }
        return Arrays.asList(getAllSupportedFeatures()).contains(_ident);
    }

    /**
     * Cleanup bluetooth connection if object is garbage collected.
     */
    @Override
    protected void finalize() throws Throwable {
        if (device.isConnected()) {
            device.disconnect(); // disconnect device if our device wrapper is garbage collected
        }
        super.finalize();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[logger=" + logger + ", subDevices=" + configuredFeatures + ", device=" + device
                + ", gattService=" + gattService + "]";
    }

}
