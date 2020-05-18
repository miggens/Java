package com.miggens.siterestapi.controllers;

import com.miggens.siterestapi.data.DynamoDBDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import com.miggens.siterestapi.models.Contact;

import java.util.Optional;

@RestController
public class DataPushController {

    @Autowired
    private DynamoDBDataService dataService;

    public DataPushController(DynamoDBDataService dynamoDBDataService) {
        this.dataService = dynamoDBDataService;
    }

    @PostMapping(value = "/save-contact")
    public String postContacts(@RequestBody Contact contact) {

        Optional<String> postSuccess = this.dataService.saveContact(contact);

        if (postSuccess.isPresent()) return postSuccess.get();

        return postSuccess.orElse("Failed to produce status text... Check entry.");
    }
}
