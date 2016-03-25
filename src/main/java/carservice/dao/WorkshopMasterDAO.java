package carservice.dao;

import carservice.domain.Master;
import carservice.domain.Workshop;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WorkshopMasterDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public Master getWorkshopMasterById(Integer id) {
        Session session = this.sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Master.class);
        criteria.add(Restrictions.eq("id", id));
        List<Master> masters = criteria.list();
        session.close();
        if (masters.isEmpty()) {
            return null;
        }
        return masters.get(0);
    }

    public Workshop getWorkshopById(Integer id) {
        Session session = this.sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Workshop.class);
        criteria.add(Restrictions.eq("id", id));
        List<Workshop> workshops = criteria.list();
        session.close();
        if (workshops.isEmpty()) {
            return null;
        }
        return workshops.get(0);
    }

    public List<Workshop> getWorkshopList() {
        Session session = this.sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Workshop.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Workshop> workshops = criteria.list();
        session.close();
        return workshops;
    }
}
