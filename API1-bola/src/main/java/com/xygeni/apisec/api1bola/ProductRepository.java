package com.xygeni.apisec.api1bola;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ProductRepository {

    private final Map<String, Product> store = new ConcurrentHashMap<>();

    public Product findById(String id) {
        return store.get(id);
    }

    public Product save(String id, Product update) {
        store.put(id, update);
        return update;
    }

    public void delete(String id) {
        store.remove(id);
    }
}
