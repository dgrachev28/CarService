package carservice.service;

import carservice.dao.WorkshopMasterDAO;
import carservice.domain.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {
    @Autowired
    private WorkshopMasterDAO workshopMasterDAO;

    public Statistics getStatistics() {
        Statistics statistics = new Statistics();

        statistics.setProfit(workshopMasterDAO.getServicesSumCost());

//        statistics.setAverageTime(workshopMasterDAO.getAverageQueueAndProcessingTime());

        statistics.setServedCarCount(workshopMasterDAO.getServedCarCount());

        return statistics;
    }


}
