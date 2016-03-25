package carservice.service;

import carservice.dao.WorkshopMasterDAO;
import carservice.domain.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletConfigAware;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Service
public class TicketGenerator extends Thread {

    @Autowired
    WorkshopMasterDAO workshopMasterDAO;

    public static final int TIME_SCALE = 12 * 30;

    private long startTimeMillis;
    private Calendar startDate;

    public static final int RANDOM_INTERVAL_START_MINUTES = 15;
    public static final int RANDOM_INTERVAL_END_MINUTES = 60;
    public static final int RANDOM_PERIOD_MINUTES = RANDOM_INTERVAL_END_MINUTES - RANDOM_INTERVAL_START_MINUTES;


    public void run() {
        try {
            initStartTimeAndDate();
            for (int i = 0; i < 100; i++) {

                generateTicket();
                sleep(getRandomTicketInterval());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initStartTimeAndDate() {
        startTimeMillis = System.currentTimeMillis();
        startDate = Calendar.getInstance();
        startDate.set(Calendar.YEAR, 2016);
        startDate.set(Calendar.MONTH, Calendar.JANUARY);
        startDate.set(Calendar.DAY_OF_MONTH, 1);
    }

    private void generateTicket() {
        Client client = new Client();
        client.setCarId(generateRandomCarId());
        client.setQueueStartDate(getCurrentDate());
        client.setBusy(false);
        workshopMasterDAO.insertClient(client);
    }

    private String generateRandomCarId() {
        return "e123ak";
    }

    private Calendar getCurrentDate() {
        return startDate;
    }

    private int getRandomTicketInterval() {
        int randomValueMinutes = (int) (RANDOM_INTERVAL_START_MINUTES + new Random().nextDouble() * RANDOM_PERIOD_MINUTES);
        int result = minutesToMilliSeconds(randomValueMinutes) / TIME_SCALE;
        System.out.println(result);
        return result;
    }

    private int minutesToMilliSeconds(int minutes) {
        return minutes * 1000 * 60;
    }


    private char generateRandomSymbol() {
        return (char)('А' + new Random().nextInt(32));
    }

    private int generateRandomNumeral() {
        return new Random().nextInt(10);
    }

    private String generateRandomCarId() {
        String result = "";
        result += generateRandomSymbol();
        for(int i = 0; i < 3; ++i) {
            result += generateRandomNumeral();
        }
        for (int i = 0; i < 2; ++i) {
            result += generateRandomSymbol();
        }
        return result;
    }
}
