package carservice.service;

import carservice.dao.IncomeTicketDAO;
import carservice.dao.WorkshopDAO;
import carservice.domain.IncomeTicket;
import carservice.domain.Statistics;
import carservice.domain.SystemState;
import carservice.domain.Workshop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class CurrentStateService {

    @Autowired
    private WorkshopDAO workshopDAO;

    @Autowired
    private SystemTimer systemTimer;

    public List<Workshop> getWorkshopList() {
        List<Workshop> workshopList = workshopDAO.getWorkshopList();
        filterWorkshopList(workshopList);
        return workshopList;
    }

    public Calendar getCurrentDateTime() {
        return systemTimer.getCurrentDateTime();
    }

    private void filterWorkshopList(List<Workshop> workshopsList) {
        for (Workshop workshop : workshopsList) {
            List<IncomeTicket> completeTickets = new ArrayList<IncomeTicket>();
            for (IncomeTicket ticket : workshop.getQueue()) {
                if (ticket.getStatus().equals("Complete")) {
                    completeTickets.add(ticket);
                }
            }
            for (IncomeTicket ticket : completeTickets) {
                workshop.getQueue().remove(ticket);
            }
        }
    }

}
