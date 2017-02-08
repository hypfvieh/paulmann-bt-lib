package com.github.hypfvieh.paulmann.features;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothDevice;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattService;

/**
 * Base class of all 'features' a Paulmann bluetooth device can have.
 *
 * @author maniac
 */
public abstract class AbstractBluetoothFeature {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final BluetoothDevice device;
    private final BluetoothGattService gattService;
    private BluetoothGattCharacteristic characteristic;
    private FeatureIdent<?> featureIdent;

    private Exception lastError;

    protected AbstractBluetoothFeature(BluetoothGattCharacteristic _characteristic) {
        characteristic = _characteristic;
        device = _characteristic.getService().getDevice();
        gattService = _characteristic.getService();
    }

    protected BluetoothGattCharacteristic getCharacteristic() {
        return characteristic;
    }

    /**
     * Write something to the device characteristics register.
     *
     * @param _value
     * @return true on successful write, false otherwise
     */
    public boolean writeValue(byte[] _value) {
        if (_value == null) {
            return false;
        }
        lastError = null;
        try {
            if (connectIfNotConnected()) {
                characteristic.writeValue(_value, null);
                return true;
            } else {
                logger.debug("Could not connect to device");
                return false;
            }
        } catch (Exception _ex) {
            logger.warn("Exception while sending message.", _ex);
            lastError = _ex;
            return false;
        }
    }

    /**
     * Read something from the device characteristics register
     *
     * @return byte array with result, or null on error
     */
    public byte[] readValue() {
        lastError = null;
        try {
            if (connectIfNotConnected()) {
                return characteristic.readValue(null);
            } else {
                logger.debug("Could not connect to device.");
                return null;
            }
        } catch (Exception _ex) {
            logger.warn("Error while reading data.", _ex);
            lastError = _ex;
            return null;
        }
    }

    /**
     * FeatureIdent enum value which will be implemented by the concrete class.
     *
     * @return
     */
    public FeatureIdent<?> getFeatureIdent() {
        return featureIdent;
    }

    /**
     * Set the featureIdent.
     *
     * @param _featureIdent
     */
    protected void setFeatureIdent(FeatureIdent<?> _featureIdent) {
        featureIdent = _featureIdent;
    }

    /**
     * Establish a connection to the device, if not already existing.
     * This catches all exceptions and so on.
     *
     * @return true if connection is established, false otherwise
     */
    private boolean connectIfNotConnected() {
        try {
            if (!device.isConnected()) {
                return device.connect();
            } else {
                return device.isConnected();
            }
        } catch (Exception _ex) {
            logger.warn("Could not connect to device.", _ex);
            return false;
        }
    }

    /**
     * GATT service UUID.
     *
     * @return
     */
    public String getServiceId() {
        return gattService.getUuid();
    }

    /**
     * GATT characteristics UUID.
     *
     * @return
     */
    public String getCharacteristicId() {
        return characteristic.getUuid();
    }

    /**
     * Bluetooth device MAC address.
     *
     * @return mac address or null
     */
    public String getMacAddress() {
        return device.getAddress();
    }

    /**
     * Bluetooth device used for the provided feature.
     *
     * @return
     */
    public BluetoothDevice getDevice() {
        return device;
    }

    /**
     * Returns the last occurred read/write exception (if any), or null if no exception was raised.
     *
     * @return
     */
    public Exception getLastError() {
        return lastError;
    }

    /**
     * Helper to convert a byteArray to byte.
     *
     * @param _byteArr
     * @return
     */
    public static byte byteArrayToByte(byte[] _byteArr) {
        if (_byteArr == null) {
            return 0;
        }
        return _byteArr[0];
    }

    /**
     * Helper to convert a byte to byte array.
     *
     * @param _byteVal
     * @return
     */
    public static byte[] byteToByteArray(byte _byteVal) {
        return new byte[] { _byteVal };
    }
}
