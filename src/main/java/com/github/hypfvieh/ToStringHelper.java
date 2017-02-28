package com.github.hypfvieh;

import java.util.List;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothDevice;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattDescriptor;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattService;

/**
 * Helper class for some more useful toString() implementations.
 *
 * @author David M.
 */
public class ToStringHelper {

    /**
     * Read key information from {@link BluetoothDevice} and return it as String.
     *
     * @param _device
     * @return
     */
    public static String toString(BluetoothDevice _device) {
        if (_device == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();

        sb.append(_device.getClass().getSimpleName());
        sb.append(" [");
        sb.append("MAC: ").append(_device.getAddress());
        sb.append(", Alias: ").append(_device.getAlias());
        sb.append(", Name: ").append(_device.getName());
        sb.append(", Connected: ").append(_device.isConnected());
        sb.append(", Paired: ").append(_device.isPaired());
        sb.append(", BtType: ").append(_device.getBluetoothType());
        sb.append(", UUIDs: { ");
        String[] uuiDs = _device.getUuids();
        for (int i = 0; i < uuiDs.length; i++) {
            sb.append(uuiDs[i]);
            if (i < uuiDs.length) {
                sb.append(",");
            }
        }
        sb.append("} ]");

        return sb.toString();
    }

    /**
     * Read key information from {@link BluetoothGattService} and return it as String.
     *
     * @param _device
     * @return
     */

    public static String toString(BluetoothGattService _service) {
        if (_service == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();

        sb.append(_service.getClass().getSimpleName());
        sb.append(" [");
        sb.append("BtType: ").append(_service.getBluetoothType());
        sb.append(", UUID: ").append(_service.getUuid());
        sb.append(", GATT Characteristics: {");

        List<BluetoothGattCharacteristic> characteristics = _service.getGattCharacteristics();
        for (int i = 0; i < characteristics.size(); i++) {
            sb.append(characteristics.get(i));
            if (i < characteristics.size()) {
                sb.append(",");
            }
        }
        sb.append("} ]");

        return sb.toString();
    }

    /**
     * Read key information from {@link BluetoothGattCharacteristic} and return it as String.
     *
     * @param _device
     * @return
     */
    public static String toString(BluetoothGattCharacteristic _ch) {
        if (_ch == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();

        sb.append(_ch.getClass().getSimpleName());
        sb.append(" [");
        sb.append("BtType: ").append(_ch.getBluetoothType());
        sb.append(", UUID: ").append(_ch.getUuid());
        sb.append(", GATT Discriptors: {");

        List<BluetoothGattDescriptor> discriptors = _ch.getGattDescriptors();
        for (int i = 0; i < discriptors.size(); i++) {
            sb.append(discriptors.get(i).getUuid());
            if (i < discriptors.size()) {
                sb.append(",");
            }
        }
        sb.append("} ]");

        return sb.toString();
    }
}
