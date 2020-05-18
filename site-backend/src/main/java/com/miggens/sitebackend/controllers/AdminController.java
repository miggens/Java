package com.miggens.sitebackend.controllers;

import com.miggens.sitebackend.data.DynamoDBDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.miggens.sitebackend.model.ArticleContent;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

@Controller
public class AdminController {

    static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    private static final String TEMPLATE_ADM_IDX = "admin_index";
    private static final String TEMPLATE_LOGIN = "login";
    private static final String TEMPLATE_LOGOUT = "logout";
    private static final String TEMPLATE_PUSH_CONTENT = "push_content";
    private static final String TEMPLATE_PUBLISH_CONTENT = "publish_content";

    private static final String CONTENT_TITLE = "title";
    private static final String CONTENT_CREATED_ON = "createdOn";
    private static final String CONTENT_ARTICLE_FULL_TEXT = "fullContent";
    private static final String CONTENT_TAGS = "tags";
    private static final String CONTENT_IMAGE_LINKS = "imageLinks";

    @Autowired
    private DynamoDBDataService dataService;

    public AdminController(DynamoDBDataService dynamoDBDataService) {
        this.dataService = dynamoDBDataService;
    }

    @GetMapping(value = "/")
    public String getAdminIndex(Model model) {

        return TEMPLATE_ADM_IDX;
    }

    @GetMapping(value = "/login")
    public String getAdminLoginPage(Model model) { return TEMPLATE_LOGIN; }

    @GetMapping(value="/logout-success")
    public String getLogoutPage(Model model){
        return TEMPLATE_LOGOUT;
    }

    @GetMapping(value = "/push-content")
    public String getPushContent(Model model) {
        model.addAttribute("articleContent", new ArticleContent());
        return TEMPLATE_PUSH_CONTENT;
    }

    @GetMapping(value = "/publish-content")
    public String getPublishContent(@ModelAttribute(CONTENT_TITLE) String title,
                                    @ModelAttribute(CONTENT_CREATED_ON) String createdOn,
                                    @ModelAttribute(CONTENT_TAGS) String tags,
                                    @ModelAttribute(CONTENT_IMAGE_LINKS) String imageLinks,
                                    @ModelAttribute(CONTENT_ARTICLE_FULL_TEXT) String fullContent,
                                    Model model) {

        ArticleContent ac = new ArticleContent();
        ac.setTitle(title);
        ac.setCreatedOn(createdOn);
        ac.setTags(tags);
        ac.setImageLinks(imageLinks);
        ac.setFullContent(fullContent);

        model.addAttribute("publishedArticle", ac);

        return TEMPLATE_PUBLISH_CONTENT;
    }

    @PostMapping(value = "/push-content")
    public String postArticleContents(@Valid @ModelAttribute("articleContent") ArticleContent ac,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) return "push_content";

        //ac.setCreatedOn(LocalDateTime.now().toString());

        redirectAttributes.addAttribute(CONTENT_TITLE, ac.getTitle());
        redirectAttributes.addAttribute(CONTENT_CREATED_ON, ac.getCreatedOn());
        redirectAttributes.addAttribute(CONTENT_TAGS, ac.getTags());
        redirectAttributes.addAttribute(CONTENT_IMAGE_LINKS, ac.getImageLinks());
        redirectAttributes.addAttribute(CONTENT_ARTICLE_FULL_TEXT, ac.getFullContent());

        if ( this.dataService.saveContent(ac) ) {
            return "redirect:/publish-content";
        }

        return "error";
    }
}
