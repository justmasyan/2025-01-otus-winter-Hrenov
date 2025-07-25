package ru.otus.hw.services;

public interface PermissionService {

    void createDefaultPermissions(Class<?> clazz, Long id);

    void deletePermissions(Class<?> clazz, Long id);
}
