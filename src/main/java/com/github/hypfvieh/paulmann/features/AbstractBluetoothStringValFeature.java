package com.github.hypfvieh.paulmann.features;

import java.nio.charset.StandardCharsets;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

/**
* Abstraction for all Paulmann bluetooth features which were String based (characteristics supports (ASCII) String values).
*
* @author David M.
*
*/
public abstract class AbstractBluetoothStringValFeature extends AbstractBluetoothFeature {


    protected AbstractBluetoothStringValFeature(BluetoothGattCharacteristic _characteristic) {
        super(_characteristic);
    }

    /**
     * Default value.
     * @return default string
     */
    public abstract String getDefaultValue();

    /**
     * Write a String.
     * @param _value to write
     * @return true on successful write, false otherwise
     */
    public boolean writeString(String _value) {
        if (_value == null) {
            return false;
        }
        return writeValue(_value.getBytes(StandardCharsets.US_ASCII));
    }

    /**
     * Use cached write to send the value.
     * @see AbstractBluetoothFeature#writeCached(byte[])
     * @param _value to write
     */
    public void writeCached(String _value) {
        if (_value != null) {
            writeCached(_value.getBytes(StandardCharsets.US_ASCII));
        }
    }

    /**
     * Read a string.
     * @return the read string, maybe null or empty
     */
    public String readString() {
        return new String(readValue(), StandardCharsets.US_ASCII);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [defaultValue=" + getDefaultValue() + "]";
    }

}
