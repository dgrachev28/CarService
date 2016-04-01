package carservice.dao;

import carservice.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class WorkshopMasterDAO {


    @PersistenceContext
    private EntityManager entityManager;


    public List<Workshop> getWorkshopList() {
        return entityManager.createQuery("select w from Workshop w").getResultList();
    }

    public void insertClient(Client client) {
        entityManager.persist(client);
    }

    public void insertIncomeTicket(int workshopId, IncomeTicket incomeTicket) {
        Query query = entityManager.createQuery("select w from Workshop w where w.id = :workshopId");
        query.setParameter("workshopId", workshopId);
        Workshop workshop = (Workshop) query.getSingleResult();
        List<IncomeTicket> queue = workshop.getQueue();
        queue.add(incomeTicket);
        workshop.setQueue(queue);
    }

    public Service getServiceById(int id) {
        Query query = entityManager.createQuery("select s from Service s where s.id = :id");
        query.setParameter("id", id);
        return (Service) query.getSingleResult();
    }


    public Workshop getWorkshopByService(Service service) {
        Query query = entityManager.createQuery("select w from Workshop w where :service MEMBER OF w.services");
        query.setParameter("service", service);
        return (Workshop) query.getSingleResult();
    }

    public int getServicesCount() {
        long servicesCount = (Long) entityManager.createQuery("select count(s) from Service s").getSingleResult();
        return (int) servicesCount;
    }

    public void setMasterBusy(int masterId) {
        Query query = entityManager.createQuery("update Master m set m.busy = true where m.id = :masterId");
        query.setParameter("masterId", masterId);
        query.executeUpdate();
    }

}
