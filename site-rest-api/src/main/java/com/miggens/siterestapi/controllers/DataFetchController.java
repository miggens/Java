package com.miggens.siterestapi.controllers;

import com.miggens.siterestapi.models.Contact;
import com.miggens.siterestapi.models.ContactEntityModel;
import com.miggens.siterestapi.models.Content;
import com.miggens.siterestapi.models.ContentEntityModel;
import com.miggens.siterestapi.models.ContentMetadata;
import com.miggens.siterestapi.models.ContentMetadataEntityModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import com.miggens.siterestapi.data.DynamoDBDataService;
import com.miggens.siterestapi.data.DataEntityModelMapper;
import com.miggens.siterestapi.util.Util;

import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin(origins = {"http://localhost:9500", "https://miggens.com"})
public class DataFetchController {

    Logger LOG = LoggerFactory.getLogger(DataFetchController.class);

    @Autowired
    private DynamoDBDataService dataService;

    @Autowired
    private DataEntityModelMapper mapperService;

    public DataFetchController(DynamoDBDataService dynamoDBDataService, DataEntityModelMapper dataEntityModelMapper) {
        this.dataService = dynamoDBDataService;
        this.mapperService = dataEntityModelMapper;
    }

    @GetMapping(value = "/contents")
    public ResponseEntity<List<ContentEntityModel>> getContents() {
        //return list of all contents
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        List<ContentEntityModel > allContent = new ArrayList<>();
        List<Content> rawContents = this.dataService.getAllContents();

        if (rawContents.isEmpty()) {
            return new ResponseEntity<List<ContentEntityModel>> ( allContent, headers, HttpStatus.NOT_FOUND);
        }

        for (Content c : rawContents) {

            ContentEntityModel cm = this.mapperService.mapToContentEntity(c);

            String hostname = Util.getHostname();
            cm.add(new Link(hostname + "/contents"));
            allContent.add(cm);

            cm = null;
        }

        return new ResponseEntity<List<ContentEntityModel>> ( allContent, headers, HttpStatus.OK);
    }

    @GetMapping(value = "/contents-metadata")
    public ResponseEntity<List<ContentMetadataEntityModel>> getAllContentMetadata() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        List<ContentMetadataEntityModel> allContentMetadataEntities = new ArrayList<>();
        List<ContentMetadata> rawContentsMetadata = this.dataService.getAllContentMetadata();

        if (rawContentsMetadata.isEmpty()) {
            return new ResponseEntity<List<ContentMetadataEntityModel>> ( allContentMetadataEntities, headers, HttpStatus.NOT_FOUND);
        }

        for (ContentMetadata cm : rawContentsMetadata) {

            ContentMetadataEntityModel cmem= this.mapperService.mapToContentMetadataEntity(cm);

            String hostname = Util.getHostname();
            cmem.add(new Link(hostname + "/contents-metadata"));
            allContentMetadataEntities.add(cmem);

            cmem = null;
        }

        return new ResponseEntity<List<ContentMetadataEntityModel>>( allContentMetadataEntities, headers, HttpStatus.OK);
    }

    @CrossOrigin(origins = {"http://localhost:9500", "https://miggens.com"})
    @GetMapping(value = "/content")
    public ResponseEntity<ContentEntityModel> getContents(@RequestParam String title) {

        //return a single contents by title
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        Content rawContent = this.dataService.getContentByKey(title);
        ContentEntityModel cm = this.mapperService.mapToContentEntity( rawContent );

        if (cm.getErrorMessage() != null) {
            return new ResponseEntity<ContentEntityModel>( cm, headers, HttpStatus.NOT_FOUND );
        }

        String hostname = Util.getHostname();
        cm.add(new Link( hostname + "/content?title="+title.replaceAll(" ", "+")));

        return new ResponseEntity<ContentEntityModel>( cm, headers, HttpStatus.OK );
    }

    @GetMapping(value = "/contacts")
    public ResponseEntity<List<ContactEntityModel>> getContacts() {
        //return a list of all contacts
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        List<ContactEntityModel> allContacts = new ArrayList<>();
        List<Contact> rawContacts = this.dataService.getAllContacts();

        if (rawContacts.isEmpty()) {
            return new ResponseEntity<List<ContactEntityModel>> ( allContacts, headers, HttpStatus.NOT_FOUND);
        }

        for (Contact c : rawContacts) {

            ContactEntityModel cem = this.mapperService.mapToContactEntity(c);

            String hostname = Util.getHostname();
            cem.add(new Link( hostname + "/contacts" ));
            allContacts.add(cem);

            cem = null;
        }


        return new ResponseEntity<List<ContactEntityModel>>(allContacts, headers, HttpStatus.OK);
    }

    @CrossOrigin(origins = {"http://localhost:9500", "https://miggens.com"})
    @GetMapping(value = "/contact")
    public ResponseEntity<ContactEntityModel> getContacts(@RequestParam String email) {
        //return a single contact by email
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        Contact contact = this.dataService.getContactByKey(email);
        ContactEntityModel cem = this.mapperService.mapToContactEntity(contact);

        if (cem.getErrorMessage() != null) {
            return new ResponseEntity<ContactEntityModel> (cem, headers, HttpStatus.NOT_FOUND);
        }

        String hostname = Util.getHostname();
        cem.add(new Link( hostname + "/contact?email="+email));

        return new ResponseEntity<ContactEntityModel> (cem, headers, HttpStatus.OK);
    }
}
