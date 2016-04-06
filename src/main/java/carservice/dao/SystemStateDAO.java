package carservice.dao;

import carservice.domain.Status;
import carservice.domain.SystemState;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class SystemStateDAO {

    @PersistenceContext
    private EntityManager entityManager;


    public SystemState getSystemState() {
        return (SystemState) entityManager.createQuery("select s from SystemState s").getSingleResult();
    }

    public void setSystemState(Status status) {
        Query query = entityManager.createQuery("update SystemState s set s.status = :status");
        query.setParameter("status", status);
        query.executeUpdate();
    }

}
