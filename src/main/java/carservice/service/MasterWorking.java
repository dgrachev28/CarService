package carservice.service;

import carservice.dao.WorkshopMasterDAO;
import carservice.domain.Client;
import carservice.domain.IncomeTicket;
import carservice.domain.Master;
import carservice.domain.Workshop;
import com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Random;

public class MasterWorking extends Thread {

    private IncomeTicket incomeTicket;

    private SystemTimer systemTimer;

    private WorkshopMasterDAO workshopMasterDAO;

    public static final int RUN_TIME_DEFLECTION_PERCENTS = 20;


    public MasterWorking(IncomeTicket incomeTicket, SystemTimer systemTimer, WorkshopMasterDAO workshopMasterDAO) {
        this.incomeTicket = incomeTicket;
        this.systemTimer = systemTimer;
        this.workshopMasterDAO = workshopMasterDAO;
    }

    public void run() {
        try {
            sleep(getServiceRunTime());
            finishProcessService();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getServiceRunTime() {
        int averageTimeMinutes = incomeTicket.getService().getAverageTime();
        int serviceRunTime = (int) (averageTimeMinutes * generateRandomRatio());
        return systemTimer.minutesToMilliSeconds(serviceRunTime) / SystemTimer.TIME_SCALE;
    }

    private double generateRandomRatio() {
        double randomDeflection = new Random().nextInt(2 * RUN_TIME_DEFLECTION_PERCENTS + 1) - RUN_TIME_DEFLECTION_PERCENTS;
        return 1 + randomDeflection / 100;
    }

    private void finishProcessService() {
        // TODO: обновить статус текущего тикета на Complete
        // TODO: если очередь пустая, то master.busy = false
        // TODO: иначе берем первый в очереди тикет и firstTicket.master = master; firstTicket.status = "InProcess"
    }


}
