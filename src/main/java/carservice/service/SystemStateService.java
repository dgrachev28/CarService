package carservice.service;

import carservice.dao.ClientDAO;
import carservice.dao.IncomeTicketDAO;
import carservice.dao.SystemStateDAO;
import carservice.dao.WorkshopDAO;
import carservice.domain.IncomeTicket;
import carservice.domain.Status;
import carservice.domain.SystemState;
import carservice.domain.Workshop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SystemStateService {

    @Autowired
    private SystemStateDAO systemStateDAO;
    @Autowired
    private ClientDAO clientDAO;
    @Autowired
    private IncomeTicketDAO incomeTicketDAO;
    @Autowired
    private TaskExecutorExample taskExecutorExample;

    public void startApplication() {
        taskExecutorExample.startGeneratingTickets();
    }

    public void stopApplication() {
        systemStateDAO.setSystemState(Status.STOPPED);
        clientDAO.deleteAllClients();
        incomeTicketDAO.deleteAllTickets();
    }

}
