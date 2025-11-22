package com.clinic.service.interfaces;

import java.util.List;

public interface BaseService<T> {
    List<T> getAll();
    T getById(long id);
    T save(T object);
    T update(T object);
    void deleteById(long id);
}
