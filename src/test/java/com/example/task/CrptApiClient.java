package com.example.task;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class CrptApiClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Semaphore semaphore;
    private final ReentrantLock lock = new ReentrantLock(true);

    public CrptApiClient(int requestLimit, TimeUnit timeUnit) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.semaphore = new Semaphore(requestLimit);

        // Schedule task to reset the semaphore
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            lock.lock();
            try {
                semaphore.release(requestLimit - semaphore.availablePermits());
            } finally {
                lock.unlock();
            }
        }, timeUnit.toMillis(1), timeUnit.toMillis(1), TimeUnit.MILLISECONDS);
    }

    public void createDocument(Document document) throws InterruptedException, IOException {
        semaphore.acquire();
        try {
            String requestBody = objectMapper.writeValueAsString(document);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://ismp.crpt.ru/api/v3/lk/documents/create"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response: " + response.body());
        } catch (IOException | InterruptedException e) {
            semaphore.release();
            throw e;
        }
    }
}