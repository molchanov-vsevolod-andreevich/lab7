public class StorageInfo {
    private int startIdx;
    private int endIdx;
    private long lastNotificationTime;

    public StorageInfo(String interval, long notificationTime) {
        String[] splittedInterval = interval.split(" ");

        startIdx = Integer.parseInt(splittedInterval[0]);
        endIdx = Integer.parseInt(splittedInterval[1]);

        lastNotificationTime = notificationTime;
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
