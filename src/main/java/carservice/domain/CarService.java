package carservice.domain;

import java.util.Calendar;
import java.util.List;

public class CarService {
    private List<Workshop> workshops;

    private Statistics statistics;

    private Calendar currentDateTime;

    public List<Workshop> getWorkshops() {
        return workshops;
    }

    public void setWorkshops(List<Workshop> workshops) {
        this.workshops = workshops;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public Calendar getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(Calendar currentDateTime) {
        this.currentDateTime = currentDateTime;
    }
}