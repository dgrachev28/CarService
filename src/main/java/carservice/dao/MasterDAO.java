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
public class MasterDAO {


    @PersistenceContext
    private EntityManager entityManager;


    public void setMasterBusy(int masterId, boolean busy) {
        Query query = entityManager.createQuery("update Master m set m.busy = :busy where m.id = :masterId");
        query.setParameter("masterId", masterId);
        query.setParameter("busy", busy);
        query.executeUpdate();
    }

}
