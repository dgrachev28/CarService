package carservice.service;

import carservice.dao.*;
import carservice.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.transaction.TransactionManager;
import java.util.Calendar;
import java.util.Random;
import java.util.Set;

@Service
@Scope("prototype")
public class TicketGenerator extends Thread {

    @Autowired
    private WorkshopDAO workshopDAO;
    @Autowired
    private IncomeTicketDAO incomeTicketDAO;
    @Autowired
    private ServiceDAO serviceDAO;
    @Autowired
    private MasterDAO masterDAO;
    @Autowired
    private ClientDAO clientDAO;
    @Autowired
    private SystemStateDAO systemStateDAO;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private SystemTimer systemTimer;

    private static int minIntervalMinutes;
    private static int maxIntervalMinutes;
    private static int randomPeriodMinutes;

    public static void setMinIntervalMinutes(int interval) {
        minIntervalMinutes = interval;
        setRandomPeriodMinutes();
    }

    public static void setMaxIntervalMinutes(int interval) {
        maxIntervalMinutes = interval;
        setRandomPeriodMinutes();
    }

    private static void setRandomPeriodMinutes() {
        randomPeriodMinutes = maxIntervalMinutes - minIntervalMinutes;
    }


    public TicketGenerator() {
        System.out.println("NEW THREAD");
    }

    public void run() {
        try {
            systemTimer.initStartDateTime();
            systemStateDAO.setSystemState(Status.RUNNING);
            while (systemStateDAO.getSystemState().getStatus() == Status.RUNNING) {
                generateTicket();
                sleep(getRandomTicketInterval());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    private void generateTicket() {
        Client client = generateClient();
        generateIncomeTickets(client);
    }

    private Client generateClient() {
        Client client = new Client();
        client.setCarId(generateRandomCarId());
        client.setBusy(false);
        clientDAO.insertClient(client);
        return client;
    }

    private void generateIncomeTickets(Client client) {
        int incomeTicketsCount = generateRandomTicketCount();
        for (int i = 0; i < incomeTicketsCount; i++) {
            addIncomeTicket(client);
        }
    }

    private int generateRandomTicketCount() {
        double random = new Random().nextDouble();
        if (random < 0.75) {
            return 1;
        } else if (random < 0.9) {
            return 2;
        }
        return 3;
    }

    private void addIncomeTicket(Client client) {
        carservice.domain.Service service = generateRandomService();
        Workshop workshop = workshopDAO.getWorkshopByService(service);

        Master freeMaster = getFreeMasterInWorkshop(workshop);

        Set<String> carsInProcess = incomeTicketDAO.getCarsInProcess();

        IncomeTicket incomeTicket = new IncomeTicket();
        incomeTicket.setClient(client);
        incomeTicket.setService(service);
        incomeTicket.setAddQueueDate(systemTimer.getCurrentDateTime());
        if (freeMaster == null || carsInProcess.contains(client.getCarId())) {
            incomeTicket.setStatus("InQueue");
        } else {
            masterDAO.setMasterBusy(freeMaster.getId(), true);
            incomeTicket.setStatus("InProcess");
            incomeTicket.setMaster(freeMaster);
            incomeTicket.setStartProcessDate(incomeTicket.getAddQueueDate());

            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            MasterWorking masterWorking = (MasterWorking) context.getBean("masterWorking");
            masterWorking.init(incomeTicket);
            masterWorking.start();

        }
        incomeTicketDAO.insertIncomeTicket(workshop.getId(), incomeTicket);

    }

    private carservice.domain.Service generateRandomService() {
        int servicesCount = serviceDAO.getServicesCount();
        return serviceDAO.getServiceById(new Random().nextInt(servicesCount) + 1);
    }

    private Master getFreeMasterInWorkshop(Workshop workshop) {
        for (Master master : workshop.getMasters()) {
            if (!master.getBusy()) {
                return master;
            }
        }
        return null;
    }

    private String generateRandomCarId() {
        String result = "";
        result += generateRandomSymbol();
        for (int i = 0; i < 3; ++i) {
            result += generateRandomNumeral();
        }
        for (int i = 0; i < 2; ++i) {
            result += generateRandomSymbol();
        }
        return result;
    }

    private int getRandomTicketInterval() {
        int randomValueMinutes = (int) (minIntervalMinutes + new Random().nextDouble() * randomPeriodMinutes);
        int result = systemTimer.minutesToMilliSeconds(systemTimer.convertWorkTime(randomValueMinutes));
        result /= SystemTimer.TIME_SCALE;
        System.out.println(result);
        return result;
    }

    private char generateRandomSymbol() {
        Character[] possibleLetters = new Character[]{'А', 'В', 'Е', 'К', 'М', 'Н', 'О', 'Р', 'С', 'Т', 'У', 'Х'};
        return (possibleLetters[new Random().nextInt(possibleLetters.length)]);
    }

    private int generateRandomNumeral() {
        return new Random().nextInt(10);
    }

}
