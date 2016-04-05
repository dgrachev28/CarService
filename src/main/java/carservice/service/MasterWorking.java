package carservice.service;

import carservice.dao.IncomeTicketDAO;
import carservice.dao.MasterDAO;
import carservice.dao.WorkshopDAO;
import carservice.domain.IncomeTicket;
import carservice.domain.Master;

import java.util.Random;

public class MasterWorking extends Thread {

    private IncomeTicket incomeTicket;

    private SystemTimer systemTimer;

    private WorkshopDAO workshopDAO;
    private IncomeTicketDAO incomeTicketDAO;
    private MasterDAO masterDAO;


    public static final int RUN_TIME_DEFLECTION_PERCENTS = 20;


    public MasterWorking(IncomeTicket incomeTicket, SystemTimer systemTimer, WorkshopDAO workshopDAO,
                         IncomeTicketDAO incomeTicketDAO, MasterDAO masterDAO) {
        this.incomeTicket = incomeTicket;
        this.systemTimer = systemTimer;
        this.workshopDAO = workshopDAO;
        this.incomeTicketDAO = incomeTicketDAO;
        this.masterDAO = masterDAO;
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
        incomeTicketDAO.setTicketStatus(incomeTicket.getId(), "Complete");
        int ticketsInQueueCount = incomeTicketDAO.getTicketsInQueueCount();
        if (ticketsInQueueCount == 0) {
            masterDAO.setMasterBusy(incomeTicket.getMaster().getId(), false);
        } else {
            IncomeTicket firstTicket = incomeTicketDAO.getFirstTicketInQueue();
            incomeTicketDAO.setTicketMaster(firstTicket.getId(), incomeTicket.getMaster());
            incomeTicketDAO.setTicketStatus(firstTicket.getId(), "InProcess");

            MasterWorking masterWorking = new MasterWorking(firstTicket, systemTimer, workshopDAO, incomeTicketDAO, masterDAO);
            masterWorking.run();
        }
        // TODO: обновить статус текущего тикета на Complete
        // TODO: если очередь пустая, то master.busy = false
        // TODO: иначе берем первый в очереди тикет и firstTicket.master = master; firstTicket.status = "InProcess"
    }


}
