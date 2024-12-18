//package com.citaa.citaa.service;
//
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;  // Sử dụng GsonFactory thay vì JacksonFactory
//import com.google.api.client.util.store.FileDataStoreFactory;
//import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
//import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
//
//import java.io.IOException;
//import java.security.GeneralSecurityException;
//import java.util.Collections;
//
//public class AnalyticsService {
//
//    private static final String APPLICATION_NAME = "citaa_demo";
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();  // Sử dụng GsonFactory
//    private static final NetHttpTransport HTTP_TRANSPORT;
//
//    static {
//        try {
//            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        } catch (GeneralSecurityException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static final String CLIENT_ID = "461500289709-t0nocg45dstaf8fqvd5cp3dpgbcicp2j.apps.googleusercontent.com";  // Thay thế bằng Client ID của bạn
//    private static final String CLIENT_SECRET = "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";  // Thay thế bằng Client Secret của bạn
//    private static final String TOKENS_DIRECTORY_PATH = "tokens";
//
//    // Xác thực và lấy Credential
//    private static Credential authorize() throws Exception {
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, Collections.singleton(AnalyticsReportingScopes.ANALYTICS_READONLY))
//                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
//                .setAccessType("offline")
//                .build();
//
//        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
//    }
//
//    // Lấy dịch vụ AnalyticsReporting
//    public static AnalyticsReporting getAnalyticsService() throws Exception {
//        Credential credential = authorize();
//        return new AnalyticsReporting.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//    }
//}
//
