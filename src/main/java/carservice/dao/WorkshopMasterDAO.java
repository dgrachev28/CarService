package carservice.dao;

import carservice.domain.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerFactoryAccessor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class WorkshopMasterDAO {

//    @Autowired
//    private SessionFactory sessionFactory;

    @PersistenceContext
    private EntityManager entityManager;

//    public Master getWorkshopMasterById(Integer id) {
//        Session session = this.sessionFactory.openSession();
//        Criteria criteria = session.createCriteria(Master.class);
//        criteria.add(Restrictions.eq("id", id));
//        List<Master> masters = criteria.list();
//        session.close();
//        if (masters.isEmpty()) {
//            return null;
//        }
//        return masters.get(0);
//    }
//
//    public Workshop getWorkshopById(Integer id) {
//        Session session = this.sessionFactory.openSession();
//        Criteria criteria = session.createCriteria(Workshop.class);
//        criteria.add(Restrictions.eq("id", id));
//        List<Workshop> workshops = criteria.list();
//        session.close();
//        if (workshops.isEmpty()) {
//            return null;
//        }
//        return workshops.get(0);
//    }

    public List<Workshop> getWorkshopList() {
        return entityManager.createQuery("select w from Workshop w").getResultList();
    }

    public void insertClient(Client client) {
        entityManager.persist(client);
    }

    public void insertServiceQueue(ServiceQueue serviceQueue) {
        Workshop workshop = (Workshop) entityManager.createQuery("select w from Workshop w").getResultList().get(0);
        List<ServiceQueue> queue = workshop.getQueue();
        for (Service service : workshop.getServices()) {
            serviceQueue.setService(service);
            break;
        }
        queue.add(serviceQueue);
        workshop.setQueue(queue);
    }

    public void addClient() {
        Client client = new Client();
        client.setBusy(true);
        client.setCarId("23ed");
        client.setQueueStartDate(Calendar.getInstance());

        entityManager.persist(client);
    }
}
