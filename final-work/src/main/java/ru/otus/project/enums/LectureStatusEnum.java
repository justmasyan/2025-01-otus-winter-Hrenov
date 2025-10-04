package ru.otus.project.enums;

public enum LectureStatusEnum {
    IN_FUTURE(1, "Еще не началась"),
    IN_PROGRESS(2, "В процессе"),
    FINISHED(3, "Завершилась");

    public final int id;

    public final String text;

    LectureStatusEnum(int id, String text) {
        this.id = id;
        this.text = text;
    }
}
