package carservice.domain;

import java.util.List;

public class CarService {
    private List<Workshop> workshops;

    public List<Workshop> getWorkshops() {
        return workshops;
    }

    public void setWorkshops(List<Workshop> workshops) {
        this.workshops = workshops;
    }
}