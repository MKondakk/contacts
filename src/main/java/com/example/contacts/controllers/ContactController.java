package com.example.contacts.controllers;

import com.example.contacts.SecretKeyGenerator;
import com.example.contacts.entities.Contact;
import com.example.contacts.services.ContactService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

//    @PostMapping
//    public Contact createContact(@RequestBody Contact contact) {
//        // Get the user ID from the token (assuming it's stored in the authentication context)
//        Long userId = getLoggedInUserId();
//
//        // Set the user ID for the contact
//        contact.setUserId(userId);
//
//        // Call the contact service to create the contact
//        return contactService.saveContact(contact);
//    }
//
//    @PutMapping("/{contact}")
//    public Contact updateContact(@RequestBody Contact contact) {
//        // Get the user ID from the token (assuming it's stored in the authentication context)
//        Long userId = getLoggedInUserId();
//
//        // Set the user ID and
//        contact.setUserId(userId);
//
//        // Call the contact service to update the contact
//        return contactService.updateContact(contact);
//    }
//
//    @DeleteMapping("/{contactId}")
//    public void deleteContact(@PathVariable Long contactId) {
//        // Get the user ID from the token (assuming it's stored in the authentication context)
//        Long userId = getLoggedInUserId();
//
//        // Call the contact service to delete the contact
//        contactService.deleteContact(contactId);
//    }
    @GetMapping()
    public List<Contact> getAllContacts(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the token from the Authorization header
        String token = authorizationHeader.replace("Bearer ", "");

        Long userId = getLoggedInUserName(token);

        // Call the contact service to retrieve the list of contacts for the user
        return contactService.getAllContacts(userId);
    }
    private Long getLoggedInUserName(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(SecretKeyGenerator.getInstance().getSecretKey().getBytes()).build().parseClaimsJws(token).getBody();
        String userId = claims.getSubject();
        return Long.parseLong(userId);
    }
}

