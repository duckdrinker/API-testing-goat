package com.acme.bola;

import org.springframework.stereotype.Repository;

@Repository
public class Repo {
    public Customer findCustomer(String id) { return new Customer(); }
    public PublicProfile findProfile(String id) { return new PublicProfile(); }
    public Project findProject(String id) { return new Project(); }
    public Project saveProject(String id, Project p) { return p; }
    public Device findDevice(String id) { return new Device(); }
    public Account findAccount(String id) { return new Account(); }
    public void deleteAccount(String id) { }
    public Account findAccountByIdAndOwner(String id, String ownerId) { return new Account(); }
}