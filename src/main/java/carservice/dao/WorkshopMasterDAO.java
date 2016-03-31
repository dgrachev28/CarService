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

    public void insertServiceQueue(int workshopId, ServiceQueue serviceQueue) {
        Query query = entityManager.createQuery("select w from Workshop w where w.id = :workshopId");
        query.setParameter("workshopId", workshopId);
        Workshop workshop = (Workshop) query.getSingleResult();
        List<ServiceQueue> queue = workshop.getQueue();
//        for (Service service : workshop.getServices()) {
//            serviceQueue.setService(service);
//            break;
//        }
        queue.add(serviceQueue);
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

    public Long getServicesCount() {
        return (Long) entityManager.createQuery("select count(s) from Service s").getSingleResult();
    }

}
