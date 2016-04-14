package carservice.service;

import carservice.dao.*;
import carservice.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class SystemStateService {

    @Autowired
    private SystemStateDAO systemStateDAO;
    @Autowired
    private ClientDAO clientDAO;
    @Autowired
    private MasterDAO masterDAO;
    @Autowired
    private IncomeTicketDAO incomeTicketDAO;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private MasterNamesStore masterNamesStore;


    @Transactional
    public void startApplication(Integer[] mastersCounts, Integer minIntervalMinutes,
                                 Integer maxIntervalMinutes, Integer timeCoefficient) {

        if (systemStateDAO.getSystemState().getStatus() == Status.RUNNING) {
            return;
        }

        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        TicketGenerator ticketGenerator = (TicketGenerator) context.getBean("ticketGenerator");
        ticketGenerator.start();

        for (int i = 0; i < mastersCounts.length; i++) {
            masterDAO.replaceWorkshopMasters(i + 1, masterNamesStore.getMasters(mastersCounts[i]));
        }

        TicketGenerator.setMaxIntervalMinutes(maxIntervalMinutes);
        TicketGenerator.setMinIntervalMinutes(minIntervalMinutes);
        MasterWorking.setRunTimeDeflectionPercents(timeCoefficient);


    }
    @Transactional
    public void stopApplication() {
        systemStateDAO.setSystemState(Status.STOPPED);
        incomeTicketDAO.deleteAllTickets();
        clientDAO.deleteAllClients();
        masterDAO.deleteMasters();
    }

}
