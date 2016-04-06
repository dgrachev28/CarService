package carservice.dao;

import carservice.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class WorkshopDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Workshop> getWorkshopList() {
        return entityManager.createQuery("select w from Workshop w").getResultList();
    }

    public Workshop getWorkshopByService(Service service) {
        Query query = entityManager.createQuery("select w from Workshop w where :service MEMBER OF w.services");
        query.setParameter("service", service);
        return (Workshop) query.getSingleResult();
    }
}
