package carservice.dao;

import carservice.domain.Client;
import carservice.domain.IncomeTicket;
import carservice.domain.Master;
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
public class ClientDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void insertClient(Client client) {
        entityManager.persist(client);
    }

    public void deleteAllClients() {
        entityManager.createQuery("delete from Client c").executeUpdate();
    }
}
