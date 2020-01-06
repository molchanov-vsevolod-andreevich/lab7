public class StorageInfo {
    private int startIdx;
    private int endIdx;
    private long lastNotificationTime;

    public StorageInfo(String interval) {
        System.out.println(interval);
        String[] splittedInterval = interval.split(Constants.DELIMITER);

        startIdx = Integer.parseInt(splittedInterval[0]);
        endIdx = Integer.parseInt(splittedInterval[1]);
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
