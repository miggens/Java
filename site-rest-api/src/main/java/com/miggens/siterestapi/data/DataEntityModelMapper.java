package com.miggens.siterestapi.data;

import com.miggens.siterestapi.models.Contact;
import com.miggens.siterestapi.models.Content;
import com.miggens.siterestapi.models.ContentMetadata;
import com.miggens.siterestapi.models.ContactEntityModel;
import com.miggens.siterestapi.models.ContentEntityModel;
import com.miggens.siterestapi.models.ContentMetadataEntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataEntityModelMapper {

    public ContentEntityModel mapToContentEntity(Content content) {

        ContentEntityModel cm = new ContentEntityModel();

        if (content.getErrorMessage() != null) {
            cm.setErrorMessage(content.getErrorMessage());
            return cm;
        }

        cm.setTitle( content.getTitle() );
        cm.setFullContentList( content.getFullContentList() );

        return cm;
    }

    public ContentMetadataEntityModel mapToContentMetadataEntity(ContentMetadata contentMetadata) {
        ContentMetadataEntityModel cmem = new ContentMetadataEntityModel();

        cmem.setTitle( contentMetadata.getTitle() );
        cmem.setCreatedOn( contentMetadata.getCreatedOn() );
        cmem.setSnippet( contentMetadata.getSnippet() );
        cmem.setTagsList( contentMetadata.getTagsList() );
        cmem.setImageLinksList( contentMetadata.getImageLinksList() );

        List<String> imgLinkList = new ArrayList<>();
        for (String img : contentMetadata.getImageLinksList()) {
            imgLinkList.add( img );
        }

        List<Link> imgLinks = new ArrayList<>();
        for (String img : imgLinkList) {
            imgLinks.add( new Link(img) );
        }

        cmem.add(imgLinks);
        cmem.setImageLinksList(imgLinkList);

        return cmem;
    }

    public ContactEntityModel mapToContactEntity(Contact contact) {

        ContactEntityModel cem = new ContactEntityModel();

        if (contact.getErrorMessage() != null) {
            cem.setErrorMessage( contact.getErrorMessage() );
            return cem;
        }
        String email = contact.getEmail();
        cem.setEmail( email );
        cem.setName( contact.getName() );
        cem.setMessage( contact.getMessage() );
        cem.add( new Link(email) );

        return cem;
    }

}
