package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

/**
 * Abstraction for all Paulmann bluetooth features which were Integer based (characteristics only supports byte
 * values).
 *
 * @author hypfvieh
 *
 */
public abstract class AbstractBluetoothIntValFeature extends AbstractBluetoothFeature {

    public static final byte ERROR_RETURN = -128;

    protected AbstractBluetoothIntValFeature(BluetoothGattCharacteristic _characteristic) {
        super(_characteristic);
    }

    /**
     * Write a byte value.
     *
     * @param _value
     * @return true on successful write, false otherwise
     */
    public boolean writeByte(byte _value) {
        _value = validateValue(_value);

        return writeValue(byteToByteArray(_value));
    }

    /**
     * Use cached write to send the value.
     *
     * @see AbstractBluetoothFeature#writeCached(byte[])
     * @param _value
     */
    public void writeCached(byte _value) {
        _value = validateValue(_value);
        writeCached(byteToByteArray(_value));
    }

    /**
     * Validates the given value for min/max values and stepsize.
     *
     * @param _value
     * @return
     */
    private byte validateValue(byte _value) {
        // range check, only send allowed values
        if (_value > getMaxValue()) {
            _value = getMaxValue();
        } else if (_value < getMinValue()) {
            _value = getMinValue();
        }

        if (_value % getStepSize() != 0) { // value is not divisible by stepsize, fix number
            _value = ((byte) (_value / getStepSize()));
        }
        return _value;
    }

    /**
     * Read a byte from the GATT characteristics.
     *
     * @return byte or value of ERROR_RETURN if error occours
     */
    public byte readByte() {
        byte[] readValue = readValue();
        if (readValue != null) {
            return byteArrayToByte(readValue);
        } else {
            return ERROR_RETURN;
        }
    }

    /**
     * StepSize used for all values.
     *
     * @return
     */
    public abstract byte getStepSize();

    /**
     * Default value.
     *
     * @return
     */
    public abstract byte getDefaultValue();

    /**
     * Minimum allowed value.
     *
     * @return
     */
    public abstract byte getMinValue();

    /**
     * Maximum allowed value.
     *
     * @return
     */
    public abstract byte getMaxValue();

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [defaultValue=" + getDefaultValue() + ", minValue=" + getMinValue()
                + ", maxValue=" + getMaxValue() + "]";
    }
}
