package com.github.hypfvieh.paulmann.devices;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothDevice;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattService;

/**
 * Factory to create Paulmann Device class instances based on the given GATT Service.
 *
 * @author David M.
 *
 */
public class DeviceFactory {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, Class<? extends AbstractPaulmannDevice>> deviceClasses = new LinkedHashMap<>();

    private static final DeviceFactory INSTANCE = new DeviceFactory();

    private DeviceFactory() {
        deviceClasses.put(LampW.DEVICE_ALIAS, LampW.class);
        deviceClasses.put(LampWC.DEVICE_ALIAS, LampWC.class);
        deviceClasses.put(LampRGB.DEVICE_ALIAS, LampRGB.class);
        deviceClasses.put(LampRGBW.DEVICE_ALIAS, LampRGBW.class);
    }

    public static DeviceFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Create the concreate implementation of {@link AbstractPaulmannDevice} based on the given _gattService.
     *
     * @param _gattService to create device
     * @return concrete implementation object or null on error
     */
    public AbstractPaulmannDevice createDevice(BluetoothGattService _gattService) {
        if (_gattService == null) {
            return null;
        }
        BluetoothDevice device = _gattService.getDevice();
        if (deviceClasses.containsKey(device.getAlias())) {

            Class<? extends AbstractPaulmannDevice> instanceClass = deviceClasses.get(device.getAlias());
            try {
                AbstractPaulmannDevice newInstance = instanceClass.getConstructor(BluetoothGattService.class).newInstance(_gattService);
                return newInstance;
            } catch (Exception _ex) {
                logger.warn("Could not instanciate class " + instanceClass, _ex);
            }
        }
        return null;
    }
}
