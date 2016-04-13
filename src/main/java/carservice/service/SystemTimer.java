package carservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class SystemTimer {

    public static final int TIME_SCALE = 12 * 30;
    public static final int START_WORK_HOUR = 9;
    public static final int END_WORK_HOUR = 21;
    public static final int END_WORK_HOUR_WEEKENDS = 18;

    private long startTimeMillis;
    private Calendar startDate;

    public void initStartDateTime() {
        startTimeMillis = System.currentTimeMillis();
        startDate = Calendar.getInstance();
        startDate.set(Calendar.YEAR, 2016);
        startDate.set(Calendar.MONTH, Calendar.JANUARY);
        startDate.set(Calendar.DAY_OF_MONTH, 11);
        startDate.set(Calendar.HOUR_OF_DAY, 9);
    }

    public Calendar getCurrentDateTime() {
        long differenceMillis = (System.currentTimeMillis() - startTimeMillis) * TIME_SCALE;
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTimeInMillis(startDate.getTimeInMillis() + differenceMillis);
        return currentDate;
    }

    public int minutesToMilliSeconds(int minutes) {
        return minutes * 1000 * 60;
    }

    public int daysToMilliSeconds(int days) {
        return days * 1000 * 60 * 60 * 24 / TIME_SCALE;
    }

    public int convertWorkTime(int clearTimeMinutes) {
        int workTimeMinutes = 0;
        Calendar now = getCurrentDateTime();
        int minutesUntilWorkDayEnd = getMinutesUntilWorkDayEnd(now);
        if (minutesUntilWorkDayEnd <= clearTimeMinutes) {
            workTimeMinutes += getHoursBetweenWorkDays(now) * 60;
        }
        workTimeMinutes += clearTimeMinutes;
        return workTimeMinutes;
    }

    private int getHoursBetweenWorkDays(Calendar now) {
        return (24 + START_WORK_HOUR) - getWorkDayEndHour(now);
    }

    private int getMinutesUntilWorkDayEnd(Calendar now) {
        int workDayEndHour = getWorkDayEndHour(now);
        int result = workDayEndHour * 60 - (now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE));
        if (result < 0) {
            return 0;
        }
        return result;
    }

    private int getWorkDayEndHour(Calendar now) {
        int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            return END_WORK_HOUR_WEEKENDS;
        }
        return END_WORK_HOUR;
    }
}
