package com.example.contacts.repositories;

import com.example.contacts.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findAllByUserId(Long userId);


//    Contact save(Contact contact);
//
//
//    Contact saveAndFlush(Contact contact);
//
//
//    void delete(Contact contact);
}
