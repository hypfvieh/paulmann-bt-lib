package com.github.hypfvieh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.bluez.exceptions.BluezDoesNotExistsException;
import org.freedesktop.dbus.AbstractPropertiesHandler;
import org.freedesktop.dbus.exceptions.DBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hypfvieh.bluetooth.DeviceManager;
import com.github.hypfvieh.bluetooth.DiscoveryFilter;
import com.github.hypfvieh.bluetooth.DiscoveryTransport;
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
 * @author David M.
 *
 */
public class PaulmannDeviceController {
    private static final PaulmannDeviceController INSTANCE = new PaulmannDeviceController();

    private static final List<String> SUPPORTED_DEVICES = Arrays.asList(
            new String[] { LampW.DEVICE_ALIAS, LampRGB.DEVICE_ALIAS, LampWC.DEVICE_ALIAS, LampRGBW.DEVICE_ALIAS });

    public static final int DEFAULT_SCAN_TIMEOUT_SEC = 10;
    private static final List<BluetoothAdapter> BT_ADAPTER_LIST = new ArrayList<>();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private DeviceManager manager;

    /** Map of MAC address -> device instance */
    private Map<String, AbstractPaulmannDevice> devices = new LinkedHashMap<>();

    private PaulmannDeviceController() {
        try {
            manager = DeviceManager.createInstance(false);
            
            Map<DiscoveryFilter, Object> filter = new HashMap<>();
            // only scan for bluetooth low energy (BLE)
            filter.put(DiscoveryFilter.Transport, DiscoveryTransport.LE);
            manager.setScanFilter(filter);
        } catch (DBusException _ex) {
            throw new RuntimeException(_ex);
        }
    }

    public static PaulmannDeviceController getInstance() {
        return INSTANCE;
    }

    /**
     * Returns a list of all found bluetooth adapters.
     * Uses the cached list of adapters if called more than once.
     * If you want to refresh the internal list use {@link #getBluetoothAdapters(boolean)}.
     * @return list of {@link BluetoothAdapter}
     */
    public static List<BluetoothAdapter> getBluetoothAdapters() {
        return getInstance().manager.getAdapters();
    }

    /**
     * Returns a list of all found bluetooth adapters either from cache or retrieves a new list from Dbus.
     * If _refresh is true, will always gather list from Dbus, otherwise will
     * use the internal cached list if the list is not empty.
     *
     * @param _refresh true to get a new list from Dbus, false otherwise
     * @return list of {@link BluetoothAdapter}
     */
    public static List<BluetoothAdapter> getBluetoothAdapters(boolean _refresh) {
        if (!_refresh && !BT_ADAPTER_LIST.isEmpty()) {
            return BT_ADAPTER_LIST;
        }
        BT_ADAPTER_LIST.addAll(getInstance().manager.getAdapters());
        return BT_ADAPTER_LIST;
    }

    /**
     * Scan for bluetooth devices for _timeout seconds.
     *
     * @param _timeout timeout for scanning
     * @throws InterruptedException on error
     */
    public void scanForDevices(int _timeout) throws InterruptedException {
        int timeout = DEFAULT_SCAN_TIMEOUT_SEC;
        if (_timeout > 0) {
            timeout = _timeout;
        }

        logger.debug("Scanning for bluetooth devices for {} seconds", timeout);
        
        manager.scanForBluetoothDevices(timeout * 1000);
        logger.debug("Scanning for bluetooth devices has finished");
    }

    /**
     * Finds and creates instances of all supported Bluetooth devices.
     */
    public void refreshDevices() {
        devices.clear();
        for (BluetoothDevice device : manager.getDevices()) {
            if (!SUPPORTED_DEVICES.contains(device.getName())) {
                logger.debug("Device '{}' does not match any supported device name, ignoring.", device.getName());
                continue; // ignore unsupported devices
            }
            try {
                if (device.connect()) {
                    List<BluetoothGattService> services = device.getGattServices();
                    for (BluetoothGattService gattService : services) {
                        if (!gattService.getUuid().toUpperCase().startsWith("0000FFB")) { // skip all non-paulmann services
                            continue;
                        }
                        AbstractPaulmannDevice paulmannDevice = DeviceFactory.getInstance().createDevice(gattService);
                        if (paulmannDevice != null) {
                            devices.put(device.getAddress(), paulmannDevice);
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
     *
     * @return unmodifiable map of macaddress / {@link AbstractPaulmannDevice}
     */
    public Map<String, AbstractPaulmannDevice> getDevices() {
        if (devices == null || devices.isEmpty()) {
            Collections.unmodifiableMap(new HashMap<>());
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
        manager.closeConnection();
    }

    /**
     * Setup the default bluetooth adapter to use.
     *
     * @param _macAddress adapters MAC address
     * @throws BluezDoesNotExistsException if adapter does not exist
     */
    public void setDefaultBluetoothAdapter(String _macAddress) throws BluezDoesNotExistsException {
        manager.setDefaultAdapter(_macAddress);
    }

    /**
     * List all found devices raw information.
     *
     * @param _showUnsupported also list unsupported devices
     * @return list of {@link DeviceDetails}, maybe empty but never null
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
     * Registers a PropertyChange callback.
     *
     * @param _handler callback handler
     * @return true if callback could be registered, false otherwise
     */
    public boolean registerPropertyHandler(AbstractPropertiesHandler _handler) {
        try {
            manager.registerPropertyHandler(_handler);
            return true;
        } catch (DBusException _ex) {
            logger.info("Could not register PropertiesChanged callback", _ex);
            return false;
        }
    }
  
    /**
     * Meta-Information class with all read properties of a bluetooth device.
     *
     * @author David M.
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

        public String prettyToStringShort() {
            List<String> text = new ArrayList<>();
            text.add("Addr: " + getMacAddr());
            text.add("Name: " + getName());
            text.add("Alias: " + getAlias());
            text.add("Library Support: " + isSupported());

            return StringUtils.join(text, System.lineSeparator());
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

            return StringUtils.join(text, System.lineSeparator());
        }

        @Override
        public String toString() {
            return "DeviceDetails [macAddr=" + macAddr + ", name=" + name + ", supported=" + supported + ", alias="
                    + alias + ", modAlias=" + modAlias + ", appearance=" + appearance + ", btClass=" + btClass
                    + ", uuids=" + uuids + ", servicesAndCharacteristics=" + servicesAndCharacteristics + ", error="
                    + error + "]";
        }
    }
}
