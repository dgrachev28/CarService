package carservice.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Workshop {
    @Id
    private Integer id;

    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="workshop_id")
    private Set<Service> services;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name="workshop_id")
    private List<IncomeTicket> queue;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Master> masters;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    public Set<Master> getMasters() {
        return masters;
    }

    public void setMasters(Set<Master> masters) {
        this.masters = masters;
    }

    public List<IncomeTicket> getQueue() {
        return queue;
    }

    public void setQueue(List<IncomeTicket> ticketQueue) {
        this.queue = ticketQueue;
    }
}
