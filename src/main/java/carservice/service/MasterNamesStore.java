package carservice.service;

import carservice.domain.Master;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MasterNamesStore {

    private static String[] names = {
            "Денис",
            "Максим",
            "Мария",
            "Андрей",
            "Сергей",
            "Наталья",
            "Юрий",
            "Алексей",
            "Федор",
            "Дмитрий",
            "Кирилл",
            "Евгений",
            "Анастасия",
            "Иван",
            "Виктор",
            "Игорь",
            "Антон",
            "Никита",
            "Олег",
            "Даниил",
            "Василий",
            "Александр"
    };

    private static int pointer = 0;

    public static String getNextName() {
        if (pointer == names.length) {
            pointer = 0;
        }
        return names[pointer++];
    }

    public static Set<Master> getMasters(Integer size) {
        Set<Master> result = new HashSet<Master>();
        for (int i = 0; i < size; i++) {
            Master master = new Master();
            master.setBusy(false);
            master.setName(getNextName());
            result.add(master);
        }
        return result;
    }

}
