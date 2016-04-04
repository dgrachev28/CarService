package carservice.domain;


public class Statistics {
    private Integer averageTime;

    private Integer profit;

    private Integer servedCarCount;

    public Integer getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(Integer averageTime) {
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
}
