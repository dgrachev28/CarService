package carservice.service;

import carservice.dao.*;
import carservice.domain.Client;
import carservice.domain.Master;
import carservice.domain.IncomeTicket;
import carservice.domain.Workshop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Random;

@Service
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
    private SystemTimer systemTimer;

    public static final int RANDOM_INTERVAL_START_MINUTES = 15;
    public static final int RANDOM_INTERVAL_END_MINUTES = 60;
    public static final int RANDOM_PERIOD_MINUTES = RANDOM_INTERVAL_END_MINUTES - RANDOM_INTERVAL_START_MINUTES;


    public void run() {
        try {
            systemTimer.initStartDateTime();
            while (true) {
                Calendar currentDate = systemTimer.getCurrentDateTime();

                if (currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                        currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    sleep(systemTimer.daysToMilliSeconds(2));
                }

                generateTicket();
                sleep(getRandomTicketInterval());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void generateTicket() {
        Client client = new Client();
        client.setCarId(generateRandomCarId());
        client.setBusy(false);
        clientDAO.insertClient(client);

        addIncomeTicket(client);


    }

    public void addIncomeTicket(Client client) {
        carservice.domain.Service service = generateRandomService();
        Workshop workshop = workshopDAO.getWorkshopByService(service);

        Master freeMaster = getFreeMasterInWorkshop(workshop);


        IncomeTicket incomeTicket = new IncomeTicket();
        incomeTicket.setClient(client);
        incomeTicket.setService(service);
        incomeTicket.setAddQueueDate(systemTimer.getCurrentDateTime());
        if (freeMaster == null) {
            incomeTicket.setStatus("InQueue");
        } else {
            masterDAO.setMasterBusy(freeMaster.getId(), true);
            incomeTicket.setStatus("InProcess");
            incomeTicket.setMaster(freeMaster);
            incomeTicket.setStartProcessDate(incomeTicket.getAddQueueDate());

            MasterWorking masterWorking = new MasterWorking(incomeTicket, systemTimer, workshopDAO, incomeTicketDAO, masterDAO);
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
        int randomValueMinutes = (int) (RANDOM_INTERVAL_START_MINUTES + new Random().nextDouble() * RANDOM_PERIOD_MINUTES);
        int result = systemTimer.minutesToMilliSeconds(randomValueMinutes) / SystemTimer.TIME_SCALE;
        System.out.println(result);
        return result;
    }


    private char generateRandomSymbol() {
        Character[] possibleLetters = new Character[]{'А', 'В', 'Е', 'К', 'М', 'Н', 'О', 'Р', 'С', 'Т', 'У', 'Х'};
        return (char) (possibleLetters[new Random().nextInt(possibleLetters.length)]);
    }

    private int generateRandomNumeral() {
        return new Random().nextInt(10);
    }

}
