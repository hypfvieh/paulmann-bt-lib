package com.github.hypfvieh.paulmann.features;

import java.math.BigInteger;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

/**
 * Abstraction for all Paulmann bluetooth features which were Integer based (characteristics only supports byte
 * values).
 *
 * @author hypfvieh
 *
 */
public abstract class AbstractIntValFeature extends AbstractFeature {

    public static final byte ERROR_RETURN = -128;

    protected AbstractIntValFeature(BluetoothGattCharacteristic _characteristic) {
        super(_characteristic);
    }

    /**
     * Write a byte value.
     *
     * @param _value to write
     * @return true on successful write, false otherwise
     */
    public boolean writeByte(int _value) {
        _value = validateValue(_value);

        return writeValue(BigInteger.valueOf(_value).toByteArray());
    }

    /**
     * Use cached write to send the value.
     *
     * @see AbstractFeature#writeCached(byte[])
     * @param _value to write
     */
    public void writeCached(int _value) {
        _value = validateValue(_value);
        writeCached(BigInteger.valueOf(_value).toByteArray());
    }

    /**
     * Validates the given value for min/max values and stepsize.
     *
     * @param _value to check
     * @return same as _input or the closest possible value
     */
    private int validateValue(int _value) {
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
     * @return byte
     */
    public abstract int getStepSize();

    /**
     * Default value.
     *
     * @return byte
     */
    public abstract int getDefaultValue();

    /**
     * Minimum allowed value.
     *
     * @return byte
     */
    public abstract int getMinValue();

    /**
     * Maximum allowed value.
     *
     * @return byte
     */
    public abstract int getMaxValue();

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [defaultValue=" + getDefaultValue() + ", minValue=" + getMinValue()
                + ", maxValue=" + getMaxValue() + "]";
    }
}
