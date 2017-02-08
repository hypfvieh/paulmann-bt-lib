package com.github.hypfvieh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.freedesktop.dbus.exceptions.DBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hypfvieh.bluetooth.DeviceManager;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothAdapter;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothDevice;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattService;
import com.github.hypfvieh.paulmann.devices.AbstractPaulmannDevice;
import com.github.hypfvieh.paulmann.devices.DeviceFactory;
import com.github.hypfvieh.paulmann.devices.LampRGB;
import com.github.hypfvieh.paulmann.devices.LampRGBW;
import com.github.hypfvieh.paulmann.devices.LampW;
import com.github.hypfvieh.paulmann.devices.LampWC;

/**
 * Main class for management of all supported bluetooth devices.
 *
 * @author maniac
 *
 */
public class PaulmannDeviceController {
    private static final PaulmannDeviceController INSTANCE = new PaulmannDeviceController();

    private static final List<String> SUPPORTED_DEVICES = Arrays.asList(
            new String[] { LampW.DEVICE_ALIAS, LampRGB.DEVICE_ALIAS, LampWC.DEVICE_ALIAS, LampRGBW.DEVICE_ALIAS });

    public static final int DEFAULT_SCAN_TIMEOUT_SEC = 10;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private DeviceManager manager;

    /** Map of MAC address -> device instance */
    private Map<String, AbstractPaulmannDevice> devices = new LinkedHashMap<>();

    private PaulmannDeviceController() {
        try {
            manager = DeviceManager.createInstance(false);
        } catch (DBusException _ex) {
            throw new RuntimeException(_ex);
        }
    }

    public static PaulmannDeviceController getInstance() {
        return INSTANCE;
    }

    /**
     * Returns a list of all found bluetooth adapters.
     * @return
     */
    public static List<BluetoothAdapter> getBluetoothAdapters() {
        return getInstance().manager.getAdapters();
    }

    /**
     * Scan for bluetooth devices for _timeout seconds.
     *
     * @param _timeout
     * @throws InterruptedException
     */
    public void scanForDevices(int _timeout) throws InterruptedException {
        int timeout = DEFAULT_SCAN_TIMEOUT_SEC;
        if (_timeout > 0) {
            timeout = _timeout;
        }

        logger.debug("Scanning for bluetooth devices for {} seconds", timeout);
        manager.scanForBluetoothDevices(timeout*1000);
        logger.debug("Scanning for bluetooth devices has finished");
    }

    /**
     * Finds and creates instances of all supported Bluetooth devices.
     */
    private void findAllManagableDevices() {
        devices.clear();
        for (BluetoothDevice device : manager.getDevices()) {
            if (!SUPPORTED_DEVICES.contains(device.getName())) {
                continue; // ignore unsupported devices
            }
            try {
                if (device.connect()) {
                    List<BluetoothGattService> services = device.getGattServices();
                    for (BluetoothGattService gattService : services) {
                        if (!gattService.getUuid().toUpperCase().startsWith("0000FFB")) { // skip all non-paulman
                                                                                          // services
                            continue;
                        }
                        AbstractPaulmannDevice createDevice = DeviceFactory.getInstance().createDevice(gattService);
                        if (createDevice != null) {
                            devices.put(device.getAddress(), createDevice);
                        } else {
                            logger.warn("Unable to create device for device={}, gattService={}",
                                    ToStringHelper.toString(device), ToStringHelper.toString(gattService));
                        }
                    }
                    device.disconnect();
                }
            } catch (Exception _ex) {
                logger.debug("Cannot connect to device {} ({}), it seems to be offline", device.getName(),
                        device.getAddress());
            }
        }
    }

    /**
     * Returns a list of all devices which could be managed by {@link PaulmannDeviceController}.
     * @return
     */
    public Map<String, AbstractPaulmannDevice> getDevices() {
        if (devices == null || devices.isEmpty()) {
            findAllManagableDevices();
        }
        return Collections.unmodifiableMap(devices);
    }

    /**
     * De-initializes all devices, features and bluetooth adapter connections.
     */
    public void deinitialize() {
        if (devices != null && !devices.isEmpty()) {
            for (AbstractPaulmannDevice device : devices.values()) {
                if (device.getDevice().isConnected()) {
                    device.getDevice().disconnect();
                }
            }
            devices.clear();
        }
    }

    /**
     * List all found devices raw information.
     *
     * @param _showUnsupported also list unsupported devices
     * @return
     */
    public List<DeviceDetails> listAllRawDevices(boolean _showUnsupported) {
        List<DeviceDetails> devLst = new ArrayList<>();

        for (BluetoothDevice dev : manager.getDevices()) {
            if (!_showUnsupported && SUPPORTED_DEVICES.contains(dev.getName())) {
                devLst.add(new DeviceDetails(dev));
            } else if (_showUnsupported) {
                devLst.add(new DeviceDetails(dev));
            }
        }

        return devLst;
    }

    /**
     * Meta-Information class with all read properties of a bluetooth device.
     *
     * @author maniac
     */
    public static class DeviceDetails {
        private String macAddr;
        private String name;
        private String alias;
        private String modAlias;
        private String appearance;
        private String btClass;
        private List<String> uuids = new ArrayList<>();

        private Map<String, List<String>> servicesAndCharacteristics = new LinkedHashMap<>();

        private String error;

        private boolean supported;

        public DeviceDetails(BluetoothDevice _dev) {
            macAddr = _dev.getAddress();
            name = _dev.getName();
            alias = _dev.getAlias();
            for (String uuid : _dev.getUuids()) {
                uuids.add(uuid);
            }
            supported = SUPPORTED_DEVICES.contains(_dev.getName());

            try {
                if (_dev.connect()) {

                    modAlias = _dev.getModAlias();
                    appearance = _dev.getAppearance() + "";
                    btClass = _dev.getBluetoothClass() + "";

                    for (BluetoothGattService bluetoothGattService : _dev.getGattServices()) {
                        ArrayList<String> charUuids = new ArrayList<>();
                        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService
                                .getGattCharacteristics()) {
                            charUuids.add(bluetoothGattCharacteristic.getUuid());
                        }
                        servicesAndCharacteristics.put(bluetoothGattService.getUuid(), charUuids);
                    }

                    _dev.disconnect();

                }
            } catch (Exception _ex) {
                error = _ex.getMessage();
            }
        }

        public String getMacAddr() {
            return macAddr;
        }

        public String getName() {
            return name;
        }

        public String getAlias() {
            return alias;
        }

        public String getModAlias() {
            return modAlias;
        }

        public String getAppearance() {
            return appearance;
        }

        public String getBtClass() {
            return btClass;
        }

        public List<String> getUuids() {
            return uuids;
        }

        public Map<String, List<String>> getServicesAndCharacteristics() {
            return servicesAndCharacteristics;
        }

        public String getError() {
            return error;
        }

        public boolean isSupported() {
            return supported;
        }

        public String prettyToString() {
            List<String> text = new ArrayList<>();
            text.add("Addr: " + getMacAddr());
            text.add("Name: " + getName());
            text.add("Alias: " + getAlias());
            text.add("Library Support: " + isSupported());
            text.add("=====================");
            text.add("Modalias: " + getModAlias());
            text.add("Appear: " + getAppearance());
            text.add("BtClass: " + getBtClass());
            text.add("=====================");
            text.add("UUIDs:");
            for (String uuid : getUuids()) {
                text.add("\t" + uuid);
            }
            text.add("=====================");
            text.add("Services/GATT-Characteristics:");
            for (Entry<String, List<String>> entry : getServicesAndCharacteristics().entrySet()) {
                text.add("\t" + entry.getKey());
                for (String gatt : entry.getValue()) {
                    text.add("\t\t" + gatt);
                }
            }

            if (!StringUtils.isBlank(getError())) {
                text.add("~~~~~~~~~~~~~~~~~~~");
                text.add("Error while reading attributes: " + getError());
                text.add("~~~~~~~~~~~~~~~~~~~");
            }

            return StringUtils.join(text, "\n");
        }

        @Override
        public String toString() {
            return "DeviceDetails [macAddr=" + macAddr + ", name=" + name + ", supported=" + supported + ", alias=" + alias + ", modAlias="
                    + modAlias + ", appearance=" + appearance + ", btClass=" + btClass + ", uuids=" + uuids
                    + ", servicesAndCharacteristics=" + servicesAndCharacteristics + ", error=" + error + "]";
        }
    }
}
