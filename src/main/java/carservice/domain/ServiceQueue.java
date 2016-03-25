package carservice.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="service_queue")
public class ServiceQueue {
    @Id
    private Integer id;
//    private Service service;

    @ManyToOne
    @JoinColumn(name="car_id")
    private Client car;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getCar() {
        return car;
    }

    public void setCar(Client car) {
        this.car = car;
    }
}
