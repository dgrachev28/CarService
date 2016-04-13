package carservice.dao;

import carservice.domain.Master;
import carservice.domain.Service;
import carservice.domain.Workshop;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void setAllMastersFree() {
        Query query = entityManager.createQuery("update Master m set m.busy = false");
        query.executeUpdate();
    }

    public void deleteMasters() {
        Query query = entityManager.createQuery("select w from Workshop w");
        List<Workshop> workshops = (List<Workshop>) query.getResultList();
        for (Workshop workshop : workshops) {
            workshop.setMasters(new HashSet<Master>());
        }

//        entityManager.createQuery("delete from Master m").executeUpdate();
    }

    public void replaceWorkshopMasters(int workshopId, Set<Master> masters) {
        Query query = entityManager.createQuery("select w from Workshop w where w.id = :workshopId");
        query.setParameter("workshopId", workshopId);
        Workshop workshop = (Workshop) query.getSingleResult();
        workshop.setMasters(masters);
    }


}
