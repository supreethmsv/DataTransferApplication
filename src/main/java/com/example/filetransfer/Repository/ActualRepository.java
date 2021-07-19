package com.example.filetransfer.Repository;

import com.example.filetransfer.Models.Todo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class ActualRepository implements TodoRepository {
    public List<Todo> list = new ArrayList<>();
    @Override
    public <S extends Todo> S save(S s) {
        System.out.println(s.getDescription());
        list.add(s);
        return s;
    }

    @Override
    public <S extends Todo> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Todo> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Todo> findAll() {
        System.out.println(list);
        return list;
    }

    @Override
    public Iterable<Todo> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Todo todo) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> iterable) {

    }

    @Override
    public void deleteAll(Iterable<? extends Todo> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
