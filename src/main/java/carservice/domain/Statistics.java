package carservice.domain;


import java.util.Map;

public class Statistics {

    private String averageTime;

    private Integer profit;

    private Integer servedCarCount;

    private Map<String, Long> servicesNumber;

    private Map<String, Long> mastersIncome;


    public String getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(String averageTime) {
        this.averageTime = averageTime;
    }

    public Integer getProfit() {
        return profit;
    }

    public void setProfit(Integer profit) {
        this.profit = profit;
    }

    public Integer getServedCarCount() {
        return servedCarCount;
    }

    public void setServedCarCount(Integer servedCarCount) {
        this.servedCarCount = servedCarCount;
    }

    public Map<String, Long> getServicesNumber() {
        return servicesNumber;
    }

    public void setServicesNumber(Map<String, Long> servicesNumber) {
        this.servicesNumber = servicesNumber;
    }

    public Map<String, Long> getMastersIncome() {
        return mastersIncome;
    }

    public void setMastersIncome(Map<String, Long> mastersIncome) {
        this.mastersIncome = mastersIncome;
    }

}
