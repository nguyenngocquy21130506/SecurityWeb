package com.commenau.service;

import java.util.List;

public interface Pageable<T> {
    public List<T> getPage(int id, int size, int page , String sortBy , String sort);
    public List<T> getPage(int id, int size, int page );
}
