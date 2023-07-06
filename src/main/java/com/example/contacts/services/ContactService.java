package com.example.contacts.services;

import com.example.contacts.MailgunResponse;
import com.example.contacts.NumVerifyResponse;
import com.example.contacts.entities.Contact;
import com.example.contacts.entities.Email;
import com.example.contacts.entities.PhoneNumber;
import com.example.contacts.interfaces.ContactServiceInterface;
import com.example.contacts.repositories.ContactRepository;
import com.example.contacts.repositories.EmailRepository;
import com.example.contacts.repositories.PhoneNumberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService implements ContactServiceInterface {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private PhoneNumberRepository phoneNumberRepository;

    @Override
    public List<Contact> getAllContacts(Long userId) {
       return contactRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public Contact saveContact(Contact contact) {
//        for (Email email : contact.getEmails()) {
//            boolean isValidEmail = validateEmail(email.getEmailAddress());
//            if (!isValidEmail) {
//                throw new IllegalArgumentException("Invalid email address: " + email.getEmailAddress());
//            }
//        }
//
//        for (PhoneNumber phoneNumber : contact.getPhoneNumbers()) {
//            boolean isValidPhoneNumber = validatePhoneNumber(phoneNumber.getPhoneNumber());
//            if (!isValidPhoneNumber) {
//                throw new IllegalArgumentException("Invalid phone number: " + phoneNumber.getPhoneNumber());
//            }
//        }
//
        return contactRepository.save(contact);
    }


    @Override
    @Transactional
    public Contact updateContact(Contact contact) {
        // Update the contact entity
        Contact updatedContact = contactRepository.saveAndFlush(contact);

        // Delete existing email entities associated with the contact
//        List<Email> existingEmails = emailRepository.findAllByContactId(updatedContact.getId());
//        emailRepository.deleteAll(existingEmails);
//
//        // Save the updated email entities associated with the contact
//        for (Email email : contact.getEmails()) {
//            boolean isValidEmail = validateEmail(email.getEmailAddress());
//            if (!isValidEmail) {
//                throw new IllegalArgumentException("Invalid email address: " + email.getEmailAddress());
//            }
//            email.setContactId(updatedContact.getId());
//            emailRepository.save(email);
//        }
//
//        // Delete existing phone number entities associated with the contact
//        List<PhoneNumber> existingPhoneNumbers = phoneNumberRepository.findAllByContactId(updatedContact.getId());
//        phoneNumberRepository.deleteAll(existingPhoneNumbers);
//
//        // Save the updated phone number entities associated with the contact
//        for (PhoneNumber phoneNumber : contact.getPhoneNumbers()) {
//            boolean isValidPhoneNumber = validatePhoneNumber(phoneNumber.getPhoneNumber());
//            if (!isValidPhoneNumber) {
//                throw new IllegalArgumentException("Invalid phone number: " + phoneNumber.getPhoneNumber());
//            }
//            phoneNumber.setContactId(updatedContact.getId());
//            phoneNumberRepository.save(phoneNumber);
//        }

        return updatedContact;
    }

    private void deleteExistingEmails(Contact contact) {
//        List<Email> existingEmails = contact.getEmails();
//        for (Email email : existingEmails) {
//            emailRepository.delete(email);
//        }
    }
    private void deleteExistingPhoneNumbers(Contact contact) {
//        List<PhoneNumber> existingPhoneNumbers = contact.getPhoneNumbers();
//        for (PhoneNumber phoneNumber : existingPhoneNumbers) {
//            phoneNumberRepository.delete(phoneNumber);
//        }
    }
    private boolean validateEmail(String emailAddress) {
        String mailgunUrl = "https://api.mailgun.net/v3/address/validate";
        String apiKey = "";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("api", apiKey);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mailgunUrl)
                .queryParam("address", emailAddress);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<MailgunResponse> responseEntity = restTemplate.exchange(
                builder.toUriString(), HttpMethod.GET, requestEntity, MailgunResponse.class);

        MailgunResponse response = responseEntity.getBody();
        return response != null && response.isValid();
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        String numVerifyUrl = "http://apilayer.net/api/validate";
        String apiKey = "";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(numVerifyUrl)
                .queryParam("access_key", apiKey)
                .queryParam("number", phoneNumber);

        ResponseEntity<NumVerifyResponse> responseEntity = restTemplate.getForEntity(
                builder.toUriString(), NumVerifyResponse.class);

        NumVerifyResponse response = responseEntity.getBody();
        return response != null && response.isValid();
    }

    @Override
    public void deleteContact(Long contactId) {
        // Retrieve the contact from the database
        Optional<Contact> optionalContact = contactRepository.findById(contactId);
        if (optionalContact.isEmpty()) {
            throw new EntityNotFoundException("Contact not found with ID: " + contactId);
        }
        Contact contact = optionalContact.get();

        // Delete existing email entities associated with the contact
        deleteExistingEmails(contact);

        // Delete existing phone number entities associated with the contact
        deleteExistingPhoneNumbers(contact);

        // Delete the contact itself
        contactRepository.delete(contact);
    }
}
