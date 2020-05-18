package com.miggens.siterestapi.data;

import com.miggens.siterestapi.models.ContentMetadata;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

import com.miggens.siterestapi.models.Contact;
import com.miggens.siterestapi.models.Content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DynamoDBDataService {

    Logger LOG = LoggerFactory.getLogger(DynamoDBDataService.class);

    private static final String CONTENT_TABLE_NAME = "Content";
    private static final String CONTENT_TITLE = "Title";
    private static final String CONTENT_CREATED_ON = "CreatedOn";
    private static final String CONTENT_SNIPPET = "Snippet";
    private static final String CONTENT_ARTICLE_FULL_TEXT = "ArticleFullText";
    private static final String CONTENT_TAGS = "Tags";
    private static final String CONTENT_IMAGE_LINKS = "ImageLinks";
    private static final String CONTENT_METADATA_TABLE_NAME = "ContentMetadata";

    private static final String CONTACT_TABLE_NAME = "Contacts";
    private static final String CONTACT_EMAIL = "Email";
    private static final String CONTACT_MESSAGE = "Message";
    private static final String CONTACT_NAME = "Name";

    public Content getContentByKey(String title) {

        Content content = new Content();

        Region region = Region.US_WEST_1;
        DynamoDbClient ddb = DynamoDbClient.builder().region(region).build();

        HashMap<String, AttributeValue> contentKey = new HashMap<>();
        contentKey.put(CONTENT_TITLE, AttributeValue.builder().s(title).build());

        GetItemRequest itemRequest = GetItemRequest.builder()
                .key(contentKey)
                .tableName(CONTENT_TABLE_NAME)
                .build();

        try {
            Map<String, AttributeValue> contentItem = ddb.getItem(itemRequest).item();

            if (contentItem.isEmpty()) {
                String errStr = "Content with title " + title + " Not Found.";
                LOG.error( errStr );
                content.setErrorMessage( errStr );

                return content;
            }

            content.setTitle( contentItem.get(CONTENT_TITLE).s() );

            List<String> articleContentsList = new ArrayList<>();
            for (AttributeValue paragraphAv : contentItem.get(CONTENT_ARTICLE_FULL_TEXT).l()) {
                articleContentsList.add( paragraphAv.s() );
            }
            content.setFullContentList(articleContentsList);
        }
        catch (ResourceNotFoundException e) {
            LOG.error("Resource Not Found: " + e);
            content.setErrorMessage( e.getLocalizedMessage() );

            return content;
        }
        catch (DynamoDbException e) {
            LOG.error("DynamoDB Exception: " + e);
            content.setErrorMessage( e.getLocalizedMessage() );

            return content;
        }

        return content;
    }

    public List<Content> getAllContents() {

        List<Content> allContent = new ArrayList<>();

        Region region = Region.US_WEST_1;
        DynamoDbClient ddb = DynamoDbClient.builder().region(region).build();

        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(CONTENT_TABLE_NAME)
                .build();

        try {
            ScanResponse scanResponse = ddb.scan(scanRequest);

            for (Map<String, AttributeValue> item : scanResponse.items()) {
                Content content = new Content();

                content.setTitle( item.get(CONTENT_TITLE).s() );

                List<String> articleContentsList = new ArrayList<>();
                for (AttributeValue av : item.get(CONTENT_ARTICLE_FULL_TEXT).l()) {
                    articleContentsList.add(av.s());
                }
                content.setFullContentList(articleContentsList);

                allContent.add(content);

                content = null;
            }
        }
        catch (DynamoDbException e) {
            LOG.error("DynamoDB Exception: "+e.getLocalizedMessage());

            return allContent;
        }

        return allContent;
    }

    public List<ContentMetadata> getAllContentMetadata() {

        List<ContentMetadata> allContentMetadata = new ArrayList<>();

        Region region = Region.US_WEST_1;
        DynamoDbClient ddb = DynamoDbClient.builder().region(region).build();

        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(CONTENT_METADATA_TABLE_NAME)
                .build();

        try {

            ScanResponse scanResponse = ddb.scan(scanRequest);

            for (Map<String, AttributeValue> item : scanResponse.items()) {
                ContentMetadata cMeta = new ContentMetadata();

                cMeta.setCreatedOn( item.get(CONTENT_CREATED_ON).s() );
                cMeta.setTitle( item.get(CONTENT_TITLE).s() );
                cMeta.setSnippet( item.get(CONTENT_SNIPPET).s() );

                List<String> tagList = new ArrayList<>();
                for (AttributeValue tagAv : item.get(CONTENT_TAGS).l() ) {
                    tagList.add( tagAv.s() );
                }
                cMeta.setTagsList( tagList );

                List<String> imgLinkList = new ArrayList<>();
                for (AttributeValue imgAv : item.get(CONTENT_IMAGE_LINKS).l()) {
                    imgLinkList.add( imgAv.s() );
                }
                cMeta.setImageLinksList(imgLinkList);

                allContentMetadata.add( cMeta );
                cMeta = null;
            }
        }
        catch (DynamoDbException e) {
            LOG.error("DynamoDB Exception: "+e.getLocalizedMessage());

            return allContentMetadata;
        }

        return allContentMetadata;
    }

    public Contact getContactByKey(String email) {

        Contact contact = new Contact();

        Region region = Region.US_WEST_1;
        DynamoDbClient ddb = DynamoDbClient.builder().region(region).build();

        HashMap<String, AttributeValue> contentKey = new HashMap<>();
        contentKey.put(CONTACT_EMAIL, AttributeValue.builder().s(email).build());

        GetItemRequest itemRequest = GetItemRequest.builder()
                .key(contentKey)
                .tableName(CONTACT_TABLE_NAME)
                .build();

        try {
            Map<String, AttributeValue> contactItem = ddb.getItem(itemRequest).item();

            if (contactItem.isEmpty()) {
                String errStr = "Contact with email " + email + " Not Found.";
                LOG.error( errStr );
                contact.setErrorMessage( errStr );

                return contact;
            }

            contact.setEmail( contactItem.get(CONTACT_EMAIL).s() );
            contact.setName( contactItem.get(CONTACT_NAME).s() );
            contact.setMessage( contactItem.get(CONTACT_MESSAGE).s() );
        }
        catch (ResourceNotFoundException e) {
            LOG.error("Resource Not Found: " + e);
            contact.setErrorMessage(e.getLocalizedMessage() );

            return contact;
        }
        catch (DynamoDbException e) {
            LOG.error("DynamoDB Exception: " + e);
            contact.setErrorMessage( e.getLocalizedMessage() );

            return contact;
        }

        return contact;
    }

    public List<Contact> getAllContacts() {

        //Content content = new Content();
        List<Contact> allContacts = new ArrayList<>();

        Region region = Region.US_WEST_1;
        DynamoDbClient ddb = DynamoDbClient.builder().region(region).build();

        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(CONTACT_TABLE_NAME)
                .build();

        try {
            ScanResponse scanResponse = ddb.scan(scanRequest);

            for (Map<String, AttributeValue> item : scanResponse.items()) {

                Contact contact = new Contact();

                contact.setEmail( item.get(CONTACT_EMAIL).s() );
                contact.setName( item.get(CONTACT_NAME).s() );
                contact.setMessage( item.get(CONTACT_MESSAGE).s() );

                allContacts.add(contact);
                contact = null;
            }
        }
        catch (DynamoDbException e) {
            LOG.error("DynamoDB Exception: "+e.getLocalizedMessage());
            return allContacts;
        }

        return allContacts;
    }

    public Optional<String> saveContact(Contact contact) {

        Map<String,AttributeValue> itemValues = new HashMap<>();

        itemValues.put(CONTACT_EMAIL, AttributeValue.builder().s( contact.getEmail() ).build() );
        itemValues.put(CONTACT_NAME, AttributeValue.builder().s( contact.getName() ).build() );
        itemValues.put(CONTACT_MESSAGE, AttributeValue.builder().s( contact.getMessage() ).build() );

        Region region = Region.US_WEST_1;
        DynamoDbClient ddb = DynamoDbClient.builder().region(region).build();

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(CONTACT_TABLE_NAME)
                .item(itemValues)
                .build();

        try {
            PutItemResponse pir = ddb.putItem(putItemRequest);

            return pir.sdkHttpResponse().statusText();
        }
        catch (ResourceNotFoundException e) {
            LOG.error("ResourceNotFoundException : "+e.getMessage());
            return Optional.of(e.getLocalizedMessage());
        }
        catch (DynamoDbException e) {
            LOG.error("DynamoDBException: "+e.getMessage());
            return Optional.of(e.getLocalizedMessage());
        }
    }
}
