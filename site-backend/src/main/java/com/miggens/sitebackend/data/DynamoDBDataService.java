package com.miggens.sitebackend.data;

import com.miggens.sitebackend.model.ArticleContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DynamoDBDataService {

    static final Logger LOG = LoggerFactory.getLogger(DynamoDBDataService.class);

    private static final String CONTENT_TABLE_NAME = "Content";
    private static final String CONTENT_METADATA_TABLE_NAME = "ContentMetadata";
    private static final String CONTENT_TITLE = "Title";
    private static final String CONTENT_CREATED_ON = "CreatedOn";
    private static final String CONTENT_SNIPPET = "Snippet";
    private static final String CONTENT_ARTICLE_FULL_TEXT = "ArticleFullText";
    private static final String CONTENT_TAGS = "Tags";
    private static final String CONTENT_IMAGE_LINKS = "ImageLinks";

    public boolean saveContent(ArticleContent articleContent) {

        articleContent.setCreatedOn(LocalDateTime.now().toString());

        List<AttributeValue> tagsAt = Arrays.stream( articleContent.getTags().split(",") ).map(tag -> {
            return tag.trim();
        }).filter( tag -> {
            return !tag.isEmpty();
        }).map( tag -> {
            return AttributeValue.builder().s(tag).build();
        }).collect(Collectors.toList());

        List<AttributeValue> articleParagraphsAt = Arrays.stream( articleContent.getFullContent().split("\n") ).map( p -> {
            return p.trim();
        }).filter( p -> {
            return !p.isEmpty();
        }).map( p -> {
            return AttributeValue.builder().s(p).build();
        }).collect(Collectors.toList());

        List<AttributeValue> iLinksAt = Arrays.stream( articleContent.getImageLinks().split(",") ).map( img -> {
            return img.trim();
        }).filter( img -> {
            return !img.trim().isEmpty();
        }).map( img -> {
            return AttributeValue.builder().s(img).build();
        }).collect(Collectors.toList());

        int paragraphCount = articleParagraphsAt.size();
        Random rand = new Random();

        HashMap<String,AttributeValue> contentItemValues = new HashMap<String,AttributeValue>();
        contentItemValues.put(CONTENT_TITLE, AttributeValue.builder().s(articleContent.getTitle()).build() );
        contentItemValues.put(CONTENT_ARTICLE_FULL_TEXT, AttributeValue.builder().l(articleParagraphsAt).build() );

        HashMap<String,AttributeValue> contentMetadataItemValues = new HashMap<String,AttributeValue>();
        contentMetadataItemValues.put(CONTENT_TITLE, AttributeValue.builder().s(articleContent.getTitle()).build());
        contentMetadataItemValues.put(CONTENT_CREATED_ON, AttributeValue.builder().s(articleContent.getCreatedOn()).build());
        contentMetadataItemValues.put(CONTENT_TAGS, AttributeValue.builder().l(tagsAt).build());
        contentMetadataItemValues.put(CONTENT_IMAGE_LINKS, AttributeValue.builder().l( iLinksAt ).build());
        contentMetadataItemValues.put(CONTENT_SNIPPET, AttributeValue.builder().s( articleParagraphsAt.get(rand.nextInt(paragraphCount)).s() ).build());

        Region region = Region.US_WEST_1;
        DynamoDbClient ddb = DynamoDbClient.builder().region(region).build();

        PutItemRequest contentPutItemRequest = PutItemRequest.builder()
                .tableName(CONTENT_TABLE_NAME)
                .item(contentItemValues)
                .build();

        PutItemRequest contentMetadataPutItemRequest = PutItemRequest.builder()
                .tableName(CONTENT_METADATA_TABLE_NAME)
                .item(contentMetadataItemValues)
                .build();

        try {
            PutItemResponse contentPutItemResp = ddb.putItem( contentPutItemRequest );
            PutItemResponse contentMetadataPutItemResp = ddb.putItem( contentMetadataPutItemRequest );

            return contentPutItemResp.sdkHttpResponse().isSuccessful()
                    && contentMetadataPutItemResp.sdkHttpResponse().isSuccessful();
        }
        catch (ResourceNotFoundException e) {
            LOG.error("ResourceNotFoundException : "+e.getMessage());
            return false;
        }
        catch (DynamoDbException e) {
            LOG.error("DynamoDBException: "+e.getMessage());
            return false;
        }
    }
}
