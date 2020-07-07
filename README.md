# Java
Public respository to store demo Java applications. The following projects have been added to demonstrate general knowledge and proficiency in the Java Programming Language (1.8) as well as Spring, Spring MVC, and Spring Boot frameworks.  

## mearud 

This project is a utlity tool for building my ClojureScript Reframe frontend projects and Clojure Ring backend webapps. This functions as a simple commandline too. It is anticipating further functionality for deploying artifacts to AWS. It is still a work in progress. 

## site-backend

This project (not thoughtfully named) is a Spring Boot MVC application that functions as an admin portal for my site https://miggens.com. The purpose of this project is to push data (articles thus far) into a DynamoDB Document/Table. It is secured with Spring Security and the User Details Service is backed by JPA and a MySQL database in Amazon RDS. This is hosted at http://admin.miggens.com. 

## site-resp-api

This project functions as the REST data retrieval service for my site https://miggens.com. Its purpose is to fetch data from the DynamoDB Table that site-backend populates. At this time there are only a few endpoints and also a work in progress. 

