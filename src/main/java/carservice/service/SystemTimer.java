package carservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class SystemTimer {

    public static final int TIME_SCALE = 12 * 30 * 5;

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



    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public void setStartTimeMillis(long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

}
