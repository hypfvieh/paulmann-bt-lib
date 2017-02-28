package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class BluetoothRgbFeature extends AbstractBluetoothFeature {

    private byte red = getDefaultValue(ColorChannel.RED);
    private byte green = getDefaultValue(ColorChannel.GREEN);
    private byte blue = getDefaultValue(ColorChannel.BLUE);

    protected BluetoothRgbFeature(BluetoothGattCharacteristic _characteristic) {
        super(_characteristic);
    }

    /**
     * Default value to use for each channel.
     *
     * @param _chan
     * @return
     */
    public byte getDefaultValue(ColorChannel _chan) {
        return getMaxValue(_chan);
    }

    /**
     * Maximum supported value for each color channel.
     *
     * @param _chan
     * @return
     */
    public byte getMaxValue(ColorChannel _chan) {
        if (_chan == null) {
            return (byte) 255;
        }
        switch (_chan) {
            case BLUE:
                return (byte) 255;
            case GREEN:
                return (byte) 255;
            case RED:
                return (byte) 255;
            default:
                return (byte) 255;
        }
    }

    /**
     * Minimum supported value for each supported color channel.
     *
     * @param _chan
     * @return default minimum value
     */
    public byte getMinValue(ColorChannel _chan) {
        if (_chan == null) {
            return 0;
        }
        switch (_chan) {
            case BLUE:
                return 0;
            case GREEN:
                return 0;
            case RED:
                return 0;
            default:
                return 0;
        }
    }

    /**
     * Set the color of the given color channel.
     *
     * @param _chan
     * @param _val
     * @return true on success, false otherwise
     */
    public boolean setValue(ColorChannel _chan, byte _val) {
        if (_chan == null) {
            return false;
        }
        if (_val < getMinValue(_chan) || _val > getMaxValue(_chan)) {
            logger.debug("Could not set new {} channel value {} - value out of range ({}/{})", _chan, _val,
                    getMinValue(_chan), getMaxValue(_chan));
            return false;
        }

        switch (_chan) {
            case BLUE:
                blue = _val;
                break;
            case GREEN:
                green = _val;
                break;
            case RED:
                red = _val;
                break;
        }

        byte[] values = new byte[] { red, green, blue };
        return writeValue(values);
    }

    /**
     * Set all channels at once.
     *
     * @param _color
     * @return
     */
    public boolean setAllColors(byte _red, byte _green, byte _blue) {

        if (_red < getMinValue(ColorChannel.RED) || _red > getMaxValue(ColorChannel.RED)) {
            logger.debug("Could not set new red channel value {} - value out of range ({}/{})", _red,
                    getMinValue(ColorChannel.RED), getMaxValue(ColorChannel.RED));
            return false;
        }
        if (_green < getMinValue(ColorChannel.GREEN) || _green > getMaxValue(ColorChannel.GREEN)) {
            logger.debug("Could not set new green channel value {} - value out of range ({}/{})", _green,
                    getMinValue(ColorChannel.GREEN), getMaxValue(ColorChannel.GREEN));
            return false;
        }
        if (_blue < getMinValue(ColorChannel.BLUE) || _blue > getMaxValue(ColorChannel.BLUE)) {
            logger.debug("Could not set new blue channel value {} - value out of range ({}/{})", _blue,
                    getMinValue(ColorChannel.BLUE), getMaxValue(ColorChannel.BLUE));
            return false;
        }

        byte[] values = new byte[] { _red, _green, _blue };
        return writeValue(values);
    }

    /**
     * Read the current value of the given color channel.
     *
     * @param _chan
     * @return current value or default value
     */
    public byte readValue(ColorChannel _chan) {
        if (_chan == null) {
            return getDefaultValue(null);
        }

        byte[] readValue = readValue();
        if (readValue.length == 3) {
            byte color = readValue[_chan.ordinal()];
            switch (_chan) {
                case BLUE:
                    blue = color;
                    break;
                case GREEN:
                    green = color;
                    break;
                case RED:
                    red = color;
                    break;
            }
            return color;
        }

        return getDefaultValue(_chan);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureIdent<?> getFeatureIdent() {
        return FeatureIdent.PAULMANN_RGB_FEATURE;
    }

    /**
     * Enum which represents the color channels.
     *
     * @author David M.
     *
     */
    public static enum ColorChannel {
        RED,
        GREEN,
        BLUE;
    }

}
