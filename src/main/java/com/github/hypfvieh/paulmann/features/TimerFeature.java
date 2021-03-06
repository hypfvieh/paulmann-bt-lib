package com.github.hypfvieh.paulmann.features;

import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;

public class TimerFeature extends AbstractFeature {

    public static final int TIMEROPTION_FADE_OFF   = 0;
    public static final int TIMEROPTION_FADE_1SEC  = 1;
    public static final int TIMEROPTION_FADE_1MIN  = 2;
    public static final int TIMEROPTION_FADE_10MIN = 3;

    private boolean         startTimeEnabled = false;
    private boolean         stopTimeEnabled = false;

    private byte            stopTimeHour = 22;
    private byte            stopTimeMinute = 0;

    private byte            startTimeHour = 20;
    private byte            startTimeMinute = 0;

    private FadeOption      startFadingOption      = FadeOption.OFF;
    private FadeOption      stopFadingOption       = FadeOption.OFF;

    public TimerFeature(BluetoothGattCharacteristic _char) {
        super(_char);
    }

    /**
     * Read the current timer setup from the device.
     * This may fail, which means no settings will be available.
     */
    public void readTimerOptions() {
        byte[] readValue = readValue();
        if (readValue.length == 8) {
            startTimeEnabled = readValue[0] > 0;
            startTimeHour = readValue[1];
            startTimeMinute = readValue[2];
            startFadingOption = FadeOption.values()[readValue[3]];

            stopTimeEnabled = readValue[4] > 0;
            stopTimeHour = readValue[5];
            stopTimeMinute = readValue[6];
            stopFadingOption = FadeOption.values()[readValue[7]];
        }

    }

    /**
     * Send the currently configured timer options (for both, start and stop timer) to the device.
     * @return true on success, false otherwise
     */
    public boolean writeTimerOptions() {
        if (!validateHour(startTimeHour) || !validateHour(stopTimeHour)) {
            logger.debug("startTimeHour = " + startTimeHour + " or stopTimeHour = " + stopTimeHour + " are out of range (0-23)");
            return false;
        }
        if (!validateMinute(startTimeMinute) || !validateMinute(stopTimeMinute)) {
            logger.debug("startTimeMinute = " + startTimeMinute + " or stopTimeMinute = " + stopTimeMinute + " are out of range (0-59)");
            return false;
        }

        byte[] timerOpts = new byte[8];

        timerOpts[0] = isStartTimeEnabled() ? (byte) 1 : 0;
        timerOpts[1] = startTimeHour;
        timerOpts[2] = startTimeMinute;
        timerOpts[3] = (byte) startFadingOption.ordinal();

        timerOpts[4] = isStopTimeEnabled() ? (byte) 1 : 0;
        timerOpts[5] = stopTimeHour;
        timerOpts[6] = stopTimeMinute;
        timerOpts[7] = (byte) stopFadingOption.ordinal();

        return writeValue(timerOpts);
    }

    /**
     * Helper to validate hours.
     * @param _hour to check
     * @return true if valid, false otherwise
     */
    private boolean validateHour(byte _hour) {
        return (_hour > 23 || _hour < 0);
    }

    /**
     * Helper to validate minutes.
     * @param _minute to check
     * @return true if valid, false otherwise
     */
    private boolean validateMinute(byte _minute) {
        return (_minute < 0 || _minute > 59);
    }

    /**
     * Setup the start time (enable lights).
     * @param _hour when to start
     * @param _minute when to stop
     */
    public void setStartTime(byte _hour, byte _minute) {
        if (!validateHour(_hour)) {
            return;
        }
        if (!validateMinute(_minute)) {
            return;
        }

        startTimeHour = _hour;
        startTimeMinute = _minute;
    }

    /**
     * Setup the stop time (disable lights).
     *
     * @param _hour hour when to stop
     * @param _minute minute when to stop
     */
    public void setStopTime(byte _hour, byte _minute) {
        if (_hour > 23 || _hour < 0) {
            return;
        }
        if (_minute < 0 || _minute > 59) {
            return;
        }

        stopTimeHour = _hour;
        stopTimeMinute = _minute;
    }

    /**
     * Check if start timer is enabled.
     * @return true if timer enabled, false otherwise
     */
    public boolean isStartTimeEnabled() {
        return startTimeEnabled;
    }

    /**
     * Enable/disable the start timer.
     * @param _startTimeEnabled true to enable timer, false disable timer
     */
    public void setStartTimeEnabled(boolean _startTimeEnabled) {
        startTimeEnabled = _startTimeEnabled;
    }

    /**
     * Check if stop timer is enabled.
     * @return true if timer enabled, false otherwise
     */
    public boolean isStopTimeEnabled() {
        return stopTimeEnabled;
    }

    /**
     * Enable/disable the stop timer.
     * @param _stopTimeEnabled true to enable timer, false to disable
     */
    public void setStopTimeEnabled(boolean _stopTimeEnabled) {
        stopTimeEnabled = _stopTimeEnabled;
    }

    /**
     * The current fading option configured for the start timer event.
     * @return {@link FadeOption}
     */
    public FadeOption getStartFadingOption() {
        return startFadingOption;
    }

    /**
     * Setup the Fading options to use when start timer event is fired.
     * @param _startFadingOption {@link FadeOption} to set
     */
    public void setStartFadingOption(FadeOption _startFadingOption) {
        startFadingOption = _startFadingOption;
    }

    /**
     * The current fading option configured for the stop timer event.
     * @return {@link FadeOption}
     */
    public FadeOption getStopFadingOption() {
        return stopFadingOption;
    }

    /**
     * Setup the Fading options to use when stop timer event is fired.
     * @param _stopFadingOption {@link FadeOption} to set
     */
    public void setStopFadingOption(FadeOption _stopFadingOption) {
        stopFadingOption = _stopFadingOption;
    }

    /**
     * Hour which is configured to stop the timer.
     * @return byte
     */
    public byte getStopTimeHour() {
        return stopTimeHour;
    }

    /**
     * Minute which is configured to stop the timer.
     * @return byte
     */
    public byte getStopTimeMinute() {
        return stopTimeMinute;
    }

    /**
     * Hour which is configured to start the timer.
     * @return byte
     */
    public byte getStartTimeHour() {
        return startTimeHour;
    }

    /**
     * Minute which is configured to start the timer.
     * @return byte
     */
    public byte getStartTimeMinute() {
        return startTimeMinute;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureIdent<?> getFeatureIdent() {
        return FeatureIdent.PAULMANN_TIMER_FEATURE;
    }

    /**
     * Fading options for timer events.
     */
    public static enum FadeOption {
        OFF, ONE_SECOND, ONE_MINUTE, TEN_MINUTES;
    }
}
