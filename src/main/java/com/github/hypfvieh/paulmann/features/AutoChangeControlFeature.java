package com.github.hypfvieh.paulmann.features;

import java.util.AbstractMap;
import java.util.Map.Entry;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class AutoChangeControlFeature extends AbstractFeature {

    public AutoChangeControlFeature(BluetoothGattCharacteristic _char) {
        super(_char);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureIdent<?> getFeatureIdent() {
        return FeatureIdent.PAULMANN_AUTOCHANGE_CONTROL_FEATURE;
    }

    public static enum Effect {
        AUTO_MODE_FLASH(0x00, "Flash", 5, 10),
        AUTO_MODE_STROBE(0x01, "Strobe", 0, 5),
        AUTO_MODE_FADE(0x02, "Fade", 20, 70),
        AUTO_MODE_SMOOTH(0x03, "Smooth", 50, 100),
        AUTO_MODE_LEFT2RIGHT_ANIMATION1(0x04, "Animation 1 - Left to Right", 0, 3),
        AUTO_MODE_RIGHT2LEFT_ANIMATION1(0x14, "Animation 1 - Right to Left", 0, 3),
        AUTO_MODE_SIDES2MIDDLE_MIDDLE2SIDES_ANIMATION1(0x24, "Animation 1 - Sides to middle/middle to sides", 0, 3),
        AUTO_MODE_LEFT2RIGHT_ANIMATION2(0x05, "Animation 2 - Left to Right", 0, 3),
        AUTO_MODE_RIGHT2LEFT_ANIMATION2(0x15, "Animation 2 - Right to Left", 0, 3),
        AUTO_MODE_SIDES2MIDDLE_MIDDLE2SIDES_ANIMATION2(0x25, "Animation 2 - Sides to middle/middle to sides", 0, 3);

        private final byte minValue;
        private final byte maxValue;
        private final String name;
        private final byte effectId;

        private Effect(int _effectId, String _name, int _minVal, int _maxVal) {
            minValue = (byte) _minVal;
            maxValue = (byte) _maxVal;
            name = _name;
            effectId = (byte) _effectId;
        }

        public byte getMinValue() {
            return minValue;
        }

        public byte getMaxValue() {
            return maxValue;
        }

        public String getName() {
            return name;
        }

        public byte getEffectId() {
            return effectId;
        }

    }

    public void setEffect(Effect _effect, byte _value) {
        if (_value > _effect.getMaxValue()) {
            return;
        }
        if (_value < _effect.getMinValue()) {
            return;
        }

        writeValue(new byte[] {_effect.getEffectId(), _value});
    }

    public Entry<Effect, Byte> getEffect() {
        byte[] readValue = readValue();
        if (readValue.length == 2) {
            Effect eff = null;
            for (Effect e : Effect.values()) {
                if (e.getEffectId() == readValue[0]) {
                    eff = e;
                    break;
                }
            }

            return new AbstractMap.SimpleEntry<>(eff, readValue[1]);
        }

        return null;
    }

    static class AutoChangeEffects {
        private final byte minValue;
        private final byte maxValue;

        private byte       effectValue;

        public AutoChangeEffects(byte _effectValue, String _name, byte _minValue, byte _maxValue) {
         //   super(_name, _effectValue);
            minValue = _minValue;
            maxValue = _maxValue;
        }

        public int getEffectValue() {
            return effectValue;
        }

        public void setEffectValue(byte _effectValue) {
            effectValue = _effectValue;
        }

        public int getMinValue() {
            return minValue;
        }

        public int getMaxValue() {
            return maxValue;
        }

    }

}
