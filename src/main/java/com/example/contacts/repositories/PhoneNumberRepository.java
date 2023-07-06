package com.example.contacts.repositories;

import com.example.contacts.entities.Email;
import com.example.contacts.entities.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
}