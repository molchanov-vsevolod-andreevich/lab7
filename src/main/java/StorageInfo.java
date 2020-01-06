public class StorageInfo {
    private int startIdx;
    private int endIdx;
    private long lastNotificationTime;

    public StorageInfo(String interval) {
        String[] splittedInterval = interval.split(Constants.DELIMITER);

        startIdx = Integer.parseInt(splittedInterval[Constants.START_INDEX_IN_ARGS]);
        endIdx = Integer.parseInt(splittedInterval[Constants.END_INDEX_IN_ARGS]);
    }

    public int getStartIdx() {
        return startIdx;
    }

    public int getEndIdx() {
        return endIdx;
    }

    public long getLastNotificationTime() {
        return lastNotificationTime;
    }

    public void setLastNotificationTime(long notificationTime) {
        lastNotificationTime = notificationTime;
    }
}
