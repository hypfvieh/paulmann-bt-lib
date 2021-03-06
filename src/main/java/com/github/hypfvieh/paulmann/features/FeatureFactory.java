package com.github.hypfvieh.paulmann.features;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattService;

/**
 * Factory to create all features of Paulmann device.
 *
 * @author David M.
 *
 */
public class FeatureFactory {
    private static final String PAULMANN_SERVICE_UUID = "0000FFB0";

    public static final FeatureFactory INSTANCE = new FeatureFactory();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private FeatureFactory() {

    }

    public static FeatureFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Creates a feature class instance for the given service/characteristics.
     *
     * @param _feature feature to create
     * @param _service service instance
     * @return concrete class of {@link AbstractFeature} or null on error
     */
    public AbstractFeature createFeature(FeatureIdent<?> _feature, BluetoothGattService _service) {
        if (_feature == null || _service == null) {
            logger.debug("Either feature ({}) or service ({}) is null", _feature, _service);
            return null;
        } else if (_service.getDevice() == null) {
            logger.debug("Device is null");
            return null;
        }

        String serviceUuid = _service.getUuid();
        if (!serviceUuid.toUpperCase().startsWith(PAULMANN_SERVICE_UUID)) {
            logger.debug("Ignoring non-Paulmann services ({})", serviceUuid);
            return null;
        }

        if (serviceUuid.startsWith(PAULMANN_SERVICE_UUID)
                && !serviceUuid.equalsIgnoreCase(_feature.getServiceId())) {
            logger.debug("Feature ({}) is not available for the given GattServiceUUID ({})",
                    _feature.getName() + "(ServiceId: " + _feature.getServiceId() + ")", serviceUuid);
            return null;
        }

        for (BluetoothGattCharacteristic bgc : _service.getGattCharacteristics()) {
            String charUuid = bgc.getUuid();
            if (charUuid.equalsIgnoreCase(_feature.getCharId())) {
                AbstractFeature newInstance = newInstance(_feature, bgc);
                if (newInstance == null) {
                    logger.info("Could not create feature {}", _feature.getName());
                }
                return newInstance;
            }
        }
        logger.debug("No suitable GATT characteristics found for {}",
                _feature.getName() + "(CharId: " + _feature.getCharId() + ")");
        return null;
    }

    /**
     * Create a new instance by reflection.
     *
     * @param _feature feature to create instance for
     * @param _char GATT characteristics for this feature
     * @return class extending {@link AbstractFeature} or null on error
     */
    private AbstractFeature newInstance(FeatureIdent<?> _feature, BluetoothGattCharacteristic _char) {

        try {
            Class<? extends AbstractFeature> deviceClass = _feature.getDeviceClass();
            if (deviceClass != null) {
                return deviceClass.getConstructor(BluetoothGattCharacteristic.class).newInstance(_char);
            }
            return null;
        } catch (Exception _ex) {
            logger.info("Could not create BluetoothFeature-Instance.", _ex);
            return null;
        }
    }
}
