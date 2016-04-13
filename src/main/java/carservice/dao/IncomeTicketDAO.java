package carservice.dao;

import carservice.domain.*;
import org.hibernate.mapping.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class IncomeTicketDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void insertIncomeTicket(int workshopId, IncomeTicket incomeTicket) {
        Query query = entityManager.createQuery("select w from Workshop w where w.id = :workshopId");
        query.setParameter("workshopId", workshopId);
        Workshop workshop = (Workshop) query.getSingleResult();
        List<IncomeTicket> queue = workshop.getQueue();
        queue.add(incomeTicket);
        workshop.setQueue(queue);
    }


    public void setTicketStatus(int ticketId, String status) {
        Query query = entityManager.createQuery("update IncomeTicket t set t.status = :status where t.id = :id");
        query.setParameter("id", ticketId);
        query.setParameter("status", status);
        query.executeUpdate();
    }

    public void setTicketMaster(int ticketId, Master master) {
        Query query = entityManager.createQuery("update IncomeTicket t set t.master = :master where t.id = :id");
        query.setParameter("id", ticketId);
        query.setParameter("master", master);
        query.executeUpdate();
    }

    public void setTicketStartProcessDate(int ticketId, Calendar startDate) {
        String textQuery = "update IncomeTicket t set t.startProcessDate = :startDate where t.id = :id";
        Query query = entityManager.createQuery(textQuery);
        query.setParameter("id", ticketId);
        query.setParameter("startDate", startDate);
        query.executeUpdate();
    }

    public void setTicketFinishDate(int ticketId, Calendar finishDate) {
        String textQuery = "update IncomeTicket t set t.finishProcessDate = :finishDate where t.id = :id";
        Query query = entityManager.createQuery(textQuery);
        query.setParameter("id", ticketId);
        query.setParameter("finishDate", finishDate);
        query.executeUpdate();
    }

    public int getTicketsInQueueCount() {
        Query query = entityManager.createQuery("select count(t) from IncomeTicket t where t.status like 'InQueue'");
        long result = (Long) query.getSingleResult();
        return (int) result;
    }

    public IncomeTicket getFirstTicketInQueue(Master master) {
        String queryText = "select t from IncomeTicket t where t.status like 'InQueue' order by t.addQueueDate";
        List<IncomeTicket> tickets = entityManager.createQuery(queryText).getResultList();
        Set<Service> availableServices = getServicesForMaster(master);
        for (int i = 0; i < tickets.size(); i++) {
            if (!availableServices.contains(tickets.get(i).getService())) {
                tickets.remove(tickets.get(i));
                --i;
            }
        }

        if (tickets.size() > 0) {
            return tickets.get(0);
        }
        return null;
    }

    public Set<Service> getServicesForMaster(Master master) {
        Query query = entityManager.createQuery("select w.services from Workshop w where :master member of w.masters");
        query.setParameter("master", master);
        return new HashSet<Service>((List<Service>) query.getResultList());
    }

    public Long getServicesSumCost() {
        String queryText = "select SUM(t.service.cost) from IncomeTicket t where t.status not like 'InQueue'";
        Long result = (Long) entityManager.createQuery(queryText).getSingleResult();
        if (result != null) {
            return result;
        }
        return 0L;
    }

    public String getAverageQueueAndProcessingTime() {
        List<IncomeTicket> completedTickets = (List<IncomeTicket>) entityManager.createQuery(
              "select t from IncomeTicket t where t.status like 'Complete'"
                ).getResultList();
        if (completedTickets.isEmpty()) {
            return "недостаточно данных";
        } else {
            long timeInMillisSum = 0;
            for (IncomeTicket ticket : completedTickets) {
                timeInMillisSum += ticket.getFinishProcessDate().getTimeInMillis() - ticket.getAddQueueDate().getTimeInMillis();
            }
            long averageTimeInMillis = timeInMillisSum / completedTickets.size(),
                 averageTimeInMinutes = averageTimeInMillis / (1000 * 60);

            String averageTimeStr = "";

            if (averageTimeInMinutes / (60 * 24) != 0) {
                averageTimeStr += averageTimeInMinutes / (60 * 24) + " d ";
            }
            if ((averageTimeInMinutes % (60 * 24)) / 60 != 0) {
                averageTimeStr += (averageTimeInMinutes % (60 * 24)) / 60 + " h ";
            }
            if (averageTimeInMinutes % 60 != 0) {
                averageTimeStr += averageTimeInMinutes % 60 + " m";
            }

            return averageTimeStr;
        }
    }

    public int getServedCarCount() {
        String queryText = "select count(t.client) from IncomeTicket t where t.status like 'Complete'";
        long carCount = (Long) entityManager.createQuery(queryText).getSingleResult();
        return (int) carCount;
    }

    public Map<String, Long> getServicesNumber() {
        String queryText = "select t.service.name, COUNT(t) from IncomeTicket t where t.status not like 'inQueue' group by t.service.name";
        List<Object[]> servicesNumberList = entityManager.createQuery(queryText).getResultList();
        Map<String, Long> servicesNumberMap = new HashMap<String, Long>();
        for (Object[] o : servicesNumberList) {
            servicesNumberMap.put((String) o[0], (Long) o[1]);
        }
        return servicesNumberMap;
    }

    public void deleteAllTickets() {
        entityManager.createQuery("delete from IncomeTicket t").executeUpdate();
    }

}
