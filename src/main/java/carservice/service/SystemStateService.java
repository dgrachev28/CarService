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

    public void startApplication(Integer[] mastersCounts, Integer minIntervalMinutes,
                                 Integer maxIntervalMinutes, Integer timeCoefficient) {
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        TicketGenerator ticketGenerator = (TicketGenerator) context.getBean("ticketGenerator");
        ticketGenerator.start();

//        masterDAO.replaceWorkshopMasters(masterNamesStore.getNextNames(2));

    }
    @Transactional
    public void stopApplication() {
        systemStateDAO.setSystemState(Status.STOPPED);
        incomeTicketDAO.deleteAllTickets();
        clientDAO.deleteAllClients();
        masterDAO.setAllMastersFree();
    }

}
