package carservice.service;

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
            "Александр",
            "Валерий",
            "Роман",
            "Анна",
            "Кристина",
            "Софья",
            "Афанасий"
    };

    private static int pointer = 0;

    public static String getNextName() {
        if (pointer == names.length) {
            pointer = 0;
        }
        return names[pointer++];
    }

    public static Set<String> getNextNames(Integer size) {
        Set<String> result = new HashSet<String>();
        for (int i = 0; i < size; i++) {
            result.add(getNextName());
        }
        return result;
    }

}
