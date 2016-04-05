package carservice.service;

import carservice.dao.IncomeTicketDAO;
import carservice.dao.ServiceDAO;
import carservice.domain.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {
    @Autowired
    private IncomeTicketDAO incomeTicketDAO;

    @Autowired
    private ServiceDAO serviceDAO;

    public Statistics getStatistics() {
        Statistics statistics = new Statistics();

        statistics.setProfit(serviceDAO.getServicesSumCost());

//        statistics.setAverageTime(workshopMasterDAO.getAverageQueueAndProcessingTime());

        statistics.setServedCarCount(incomeTicketDAO.getServedCarCount());

        return statistics;
    }


}
