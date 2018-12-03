package com.github.hypfvieh.paulmann.features;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

/**
 * Abstraction for all Paulmann bluetooth features which only supports pre-defined Integer based values.
 *
 * @author David M.
 *
 */
public abstract class AbstractPredefinedIntValFeature extends AbstractFeature {

    private final List<PredefinedValue> predefinedValues = new ArrayList<>();

    protected AbstractPredefinedIntValFeature(BluetoothGattCharacteristic _characteristic) {
        super(_characteristic);
        populatePredefinedList();
    }

    /**
     * The default value to use if none was set before.
     *
     * @return default int
     */
    public int getDefaultValue() {
        return predefinedValues.isEmpty() ? 0 : predefinedValues.get(0).getValue();
    }

    /**
     * Minimum allowed value.
     *
     * @return int
     */
    public int getMinValue() {
        return 0;
    }

    /**
     * Maximum allowed value.
     *
     * @return int
     */
    public int getMaxValue() {
        return predefinedValues.size();
    }

    /**
     * Write a value from the predefined value list to the device by giving the position index of the value.
     *
     * @param _index to write
     * @return true on success, false otherwise
     */
    public boolean writePredefinedValueByIndex(int _index) {
        if ((_index > getMaxValue() || _index < getMinValue())) {
            logger.debug("Cannot set value {}, it's not a valid predefined value", _index);
            return false;
        }

        return writePredefinedValue(predefinedValues.get(_index).getValue());
    }

    /**
     * Write the given value to the device.
     * Checks if the given value was defined in predefined value list.
     *
     * @param _value to write
     * @return true on success, false otherwise
     */
    public boolean writePredefinedValue(int _value) {
        boolean found = false;
        for (PredefinedValue pVal : predefinedValues) {
            if (pVal.getValue() == _value) {
                found = true;
                break;
            }
        }
        if (!found) {
            logger.debug("Cannot set value {}, it's not a valid predefined value", _value);
            return false;
        }

        return writeValue(BigInteger.valueOf(_value).toByteArray());
    }

    /**
     * Add a new value to the predefined value list.
     * This method will prevent duplicates in the value list.
     *
     * @param _val to add
     */
    protected void addPredefinedValue(PredefinedValue _val) {
        if (predefinedValues.contains(_val)) {
            return;
        }
        predefinedValues.add(_val);
    }

    /**
     * Remove the given value from the list (if existing)
     *
     * @param _val to remove
     * @return true if value was in list and was removed, false otherwise
     */
    protected boolean removePredefinedValue(PredefinedValue _val) {
        return predefinedValues.remove(_val);
    }

    /**
     * Check if the given value is already in the predefined value list.
     *
     * @param _val to check
     * @return true if already contained, false otherwise
     */
    protected boolean containsPredefinedValue(int _val) {
        Optional<PredefinedValue> present = predefinedValues.stream()
                .filter(f -> f.getValue() == _val)
                .findFirst();
        return present.isPresent();
    }

    /**
     * Check if predefined value list is empty.
     *
     * @return true if list is empty, false otherwise
     */
    protected boolean isEmpty() {
        return predefinedValues.isEmpty();
    }

    /**
     * Fill values to the predefined value list.
     */
    protected abstract void populatePredefinedList();

    /**
     * Defines a pre-defined value with name and value.
     *
     * @author David M.
     *
     */
    public static class PredefinedValue {
        private final String name;
        private final int value;

        public PredefinedValue(String _name, int _value) {
            this.name = _name;
            this.value = _value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + value;
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
            PredefinedValue other = (PredefinedValue) obj;
            if (name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!name.equals(other.name)) {
                return false;
            }
            if (value != other.value) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "PredefinedValue [name=" + name + ", value=" + value + "]";
        }

    }
}
