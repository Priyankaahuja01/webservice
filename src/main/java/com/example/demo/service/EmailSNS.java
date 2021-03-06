package com.example.demo.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.regions.Region;

import java.time.Instant;
import java.util.*;


@Service
public class EmailSNS {
    SnsClient snsClient;
    @Value("${aws.sns.topic.csye6225-myTopic.ARN}")
    String snsTopicARN;
    private final static Logger logger = LoggerFactory.getLogger(EmailSNS.class);
    public void postToTopic(String recipientEmail, String requestType) {
        try {
            System.out.println("in sns postToTopic");
            Random rand = new Random();
            int randomInt = rand.nextInt(10000);
            String snsMessage = requestType + "|" + recipientEmail + "|"+ randomInt;
            System.out.println("message generated, now publishing");
            PublishRequest request = PublishRequest.builder()
                    .message(snsMessage)
                    .topicArn(snsTopicARN)
                    .build();
            if (snsClient == null) {
                System.out.println("snsClient object is still   ..........null");
            }
            SnsClient snsClient = SnsClient.builder()
                    .region(Region.US_EAST_1)
                    .build();
            PublishResponse result = snsClient.publish(request);
            System.out.println("Publishing done");
            System.out.println("Message " + result.messageId() + "is successfully published to SNS Topic 'Notification_Email'");
            logger.info("Message " + result.messageId() + " is successfully published to SNS Topic 'Notification_Email'.");
            logger.info(snsMessage);
            
            
        } catch (SnsException e) {
            System.out.println("sns exception: " + e.getMessage());
            e.printStackTrace();
            logger.error("SNS Exception Warning - " + e.getMessage());
        }
    }
    

}
