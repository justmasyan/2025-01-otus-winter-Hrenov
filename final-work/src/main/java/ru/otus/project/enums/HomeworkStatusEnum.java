package ru.otus.project.enums;

public enum HomeworkStatusEnum {
    NOT_SEND(1, "Не сдано"),
    UNDER_REVIEW(2, "На проверке"),
    NEED_FIXES(3, "Нужны исправления"),
    DONE(4, "Зачтено");

    public final int id;

    public final String text;

    HomeworkStatusEnum(int id, String text) {
        this.id = id;
        this.text = text;
    }
}
