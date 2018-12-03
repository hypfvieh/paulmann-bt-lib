package com.github.hypfvieh.paulmann.features;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class SystemTimeFeature extends AbstractFeature {

    public SystemTimeFeature(BluetoothGattCharacteristic _char) {
        super(_char);
    }


    /**
     * Setup current date and time to the device.
     *
     * @param _date date/time to set
     * @return true on success, false otherwise
     */
    public boolean setDateTime(Date _date) {
        if (_date == null) {
            return false;
        }
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(_date);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        byte[] dateArr = new byte[7];

        dateArr[0] = (byte) (year % 256); // remainder (e.g. 225 in 2017)
        dateArr[1] = (byte) (year / 256); // result of division (e.g. 7 in 2017)
        dateArr[2] = (byte) month;
        dateArr[3] = (byte) day;
        dateArr[4] = (byte) hour;
        dateArr[5] = (byte) min;
        dateArr[6] = (byte) sec;

        return writeValue(dateArr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureIdent<?> getFeatureIdent() {
        return FeatureIdent.PAULMANN_SYSTEMTIME_FEATURE;
    }

}
