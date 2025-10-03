package db.data;

import java.util.List;

public class DataDictionary {

    protected static final List<String> DEFAULT_COURSE_TITLES = List.of(
            "Администратор баз данных", "Администратор вычислительной сети",
            "Администратор защиты", "Аналитик программного обеспечения",
            "Архитектор программного обеспечения", "Веб-мастер",
            "Ведущий программист", "Директор по информационным технологиям",
            "ИТ-евангелист", "Контент-менеджер", "Программист",
            "Разработчик компьютерных игр", "Руководитель цифровой трансформации");

    protected static final List<String> DEFAULT_LECTURE_TOPICS = List.of(
            "JDK", "IntelliJ IDEA", "Gradle", "Git", "C", "C++",
            "Ruby", "Python", "JavaScript", "Docker", "Postman", "Jenkins",
            "Kotlim", "Html", "Css", "Visual Studio", "Linux");

    protected static final List<String> DEFAULT_USERNAMES = List.of(
            "almost_awake", "pixel.noise", "casual_vibes",
            "low.battery", "not_a.bot", "loading.zone", "brain_buffering", "soft.focus",
            "good_enough", "logic_404", "muted.mood", "rainy_wifi"
    );

    protected static final List<String> DEFAULT_FIRST_NAMES = List.of(
            "Катя", "Квитослав", "Квитослава", "Ким",
            "Киндин", "Киприан", "Кир", "Кира", "Кирей",
            "Кирена", "Кириак", "Кириена", "Кирилл");

    protected static final List<String> DEFAULT_LAST_NAMES = List.of(
            "Эйбрамсон", "Адамсон", "Аддерли", "Аддингтон",
            "Эйдриан", "Албертсон", "Олдридж", "Оллфорд", "Олсопп",
            "Андерсон", "Андруз", "Арчибелд", "Арнолд",
            "Артурз", "Атчесон", "Аттвуд");

    protected static final List<String> DEFAULT_JOBS = List.of("\"ПАО «Нефтяная Компания «Роснефть»",
            "ООО «Лукойл-Резервнефтепродукт-Трейдинг»",
            "ООО «Специализированное Финансовое общество Развития Базовых Отраслей Промышленности»",
            "ООО «Кредит Свисс Секьюритиз (Москва)»", "ООО «Ренессанс Брокер»",
            "ПАО «Мегафон»", "ООО «Анигма»", "АО Инвестиционная Фирма «Олма»", "АО «Росэнерготорг»");

    protected static final List<String> DEFAULT_STUDY_PLACES = List.of(
            "Отсутствует", "Школа", "Колледж", "Университет"
    );

    protected static final List<List<String>> DEFAULT_HOMEWORK_DISCUSSION = List.of(
            List.of("Здравствуйте. Отправил на проверку", "Здравствуйте. Зачтено"),
            List.of("Привет. Сдал", "Ку. Внесите исправления", "Привет. Перепроверьте", "Ку, на 3-ку пойдет"),
            List.of("Nihao, how are you?"),
            List.of("Доброе утро. Проверьт, пожалуйста", "Вечер добрый. Переделайте")
    );

    protected static final List<String> DEFAULT_LECTURE_MATERIALS = List.of(
            "Academy.yandex.ru", "Cybermarketing.ru",
            "Code.org", "Geekbrains.ru", "Hexlet.io",
            "Htmlacademy.ru", "Lendwings.com", "Loftblog.ru", "Mail.ru",
            "Microsoft.com", "Techdays.ru", "Topexpert.pro"
    );

    protected static final List<String> DEFAULT_LECTURE_URL = List.of(
          "rutube.com",  "eutube.com","uktube.com","wetube.com","theytube.com","itube.com","shetube.com",
            "hetube.com","ittube.com","foottube.com","baskettube.com","hocktube.com","letube.com","facetube.com"
    );
}
