package com.example.aws.eventbridge.example.awseventbridgeexample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.eventbridge.AmazonEventBridge;
import com.amazonaws.services.eventbridge.AmazonEventBridgeClient;
import com.amazonaws.services.eventbridge.model.PutEventsRequest;
import com.amazonaws.services.eventbridge.model.PutEventsRequestEntry;
import com.amazonaws.services.eventbridge.model.PutEventsResult;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class AwseventbridgeexampleApplication {

	@Value("${AWS_ACCESS_KEY_ID}")
    private String accessKeyId;

	@Value("${AWS_SECRET_ACCESS_KEY}")
    private String secretAccessKey;

	public static void main(String[] args) {
		// SpringApplication.run(AwseventbridgeexampleApplication.class, args);
		AmazonEventBridge client = AmazonEventBridgeClient.builder()
								.withRegion(Regions.US_EAST_1)
								.withCredentials(new DefaultAWSCredentialsProviderChain())
								.build();
		// System.setProperty("aws.accessKeyId", "AKIAYCRX2VFI7HWWQ45Y");
		// System.setProperty("aws.secretKey", "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");
		putEvents(client); // Call putEvents function

	}

	public static void putEvents(AmazonEventBridge client) {

		String detail = """
                {
					"username" : "anshu",
					"city" : "Delhi",
					"age"  : "25",
					"created_at" : "10/09/2024",
					"product" : "PL", 
					"integrationName" : "Dms",
					"integration_statusCode" : "500"
				}
				""";
				

		// PutEventsRequestEntry
		PutEventsRequestEntry requestEntry = new PutEventsRequestEntry();
		requestEntry.withSource("user-event")
					.withDetailType("user-preferences")
					.withDetail(detail)
					.withEventBusName("my-event-bus");

		PutEventsRequest req = new PutEventsRequest();
		req.withEntries(requestEntry);

		PutEventsResult result = client.putEvents(req);
		System.out.println(result.toString());
	}

}
