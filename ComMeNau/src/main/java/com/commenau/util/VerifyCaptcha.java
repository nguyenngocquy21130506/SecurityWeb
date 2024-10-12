package com.commenau.util;

import com.google.cloud.recaptchaenterprise.v1.RecaptchaEnterpriseServiceClient;
import com.google.recaptchaenterprise.v1.Assessment;
import com.google.recaptchaenterprise.v1.CreateAssessmentRequest;
import com.google.recaptchaenterprise.v1.Event;
import com.google.recaptchaenterprise.v1.ProjectName;

import java.io.IOException;

public class VerifyCaptcha {
    static String projectID = "my-project-97641-1714138861560";
    static String recaptchaKey = "6LfanccpAAAAAGuSLG2A6Ils0v-oKPYyrTOXw8kg";

    public static boolean checkCaptcha(String token) throws IOException {
        try (RecaptchaEnterpriseServiceClient client = RecaptchaEnterpriseServiceClient.create()) {


            // Set the properties of the event to be tracked.
            Event event = Event.newBuilder().setSiteKey(recaptchaKey).setToken(token).build();

            // Build the assessment request.
            CreateAssessmentRequest createAssessmentRequest =
                    CreateAssessmentRequest.newBuilder()
                            .setParent(ProjectName.of(projectID).toString())
                            .setAssessment(Assessment.newBuilder().setEvent(event).build())
                            .build();

            Assessment response = client.createAssessment(createAssessmentRequest);
            // Check if the token is valid.
            if (!response.getTokenProperties().getValid()) {
                return false;
            }
            return response.getRiskAnalysis().getScore() > 0.6;
        }
    }

}