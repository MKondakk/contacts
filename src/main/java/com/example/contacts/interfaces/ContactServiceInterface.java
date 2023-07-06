package com.example.contacts.interfaces;

import com.example.contacts.entities.Contact;

import java.util.List;

public interface ContactServiceInterface {
    List<Contact> getAllContacts(Long userId);

    Contact saveContact(Contact contact);

    Contact updateContact(Contact contact);

    void deleteContact(Long contactId);
}
