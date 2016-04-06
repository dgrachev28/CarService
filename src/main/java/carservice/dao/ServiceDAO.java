package carservice.dao;

import carservice.domain.Service;
import carservice.domain.Workshop;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Service getServiceById(int id) {
        Query query = entityManager.createQuery("select s from Service s where s.id = :id");
        query.setParameter("id", id);
        return (Service) query.getSingleResult();
    }

    public int getServicesCount() {
        long result = (Long) entityManager.createQuery("select count(s) from Service s").getSingleResult();
        return (int) result;
    }

}
