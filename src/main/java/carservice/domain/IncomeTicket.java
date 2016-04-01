package carservice.domain;

import javax.persistence.*;
import java.util.Calendar;

@Entity
public class IncomeTicket {
    @Id @GeneratedValue
    private Integer id;

    @ManyToOne
    private Service service;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Master master;

    private Calendar addQueueDate;

    private Calendar startProcessDate;

    private Calendar finishProcessDate;

    // status принимает значения: "InQueue", "InProcess", "Complete"
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public Calendar getAddQueueDate() {
        return addQueueDate;
    }

    public void setAddQueueDate(Calendar addQueueDate) {
        this.addQueueDate = addQueueDate;
    }

    public Calendar getStartProcessDate() {
        return startProcessDate;
    }

    public void setStartProcessDate(Calendar startProcessDate) {
        this.startProcessDate = startProcessDate;
    }

    public Calendar getFinishProcessDate() {
        return finishProcessDate;
    }

    public void setFinishProcessDate(Calendar finishProcessDate) {
        this.finishProcessDate = finishProcessDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
