package Converters;

import java.util.Date;

public  class TimeAgo {
    public static String timeAgo(Date createdDate) {
        long timeInMillis = createdDate.getTime();
        final long ONE_MINUTE = 60 * 1000;
        final long ONE_HOUR = 60 * ONE_MINUTE;
        final long ONE_DAY = 24 * ONE_HOUR;
        final long ONE_MONTH = 30 * ONE_DAY;
        final long ONE_YEAR = 365 * ONE_DAY;

        long now = System.currentTimeMillis();
        long diff = now - timeInMillis;

        if (diff < ONE_MINUTE) {
            return (diff / 1000) + " giây trước";
        } else if (diff < ONE_HOUR) {
            return (diff / ONE_MINUTE) + " phút trước";
        } else if (diff < ONE_DAY) {
            return (diff / ONE_HOUR) + " giờ trước";
        } else if (diff < ONE_MONTH) {
            return (diff / ONE_DAY) + " ngày trước";
        } else if (diff < ONE_YEAR) {
            return (diff / ONE_MONTH) + " tháng trước";
        } else {
            return (diff / ONE_YEAR) + " năm trước";
        }
    }
}
