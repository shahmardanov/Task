package com.example.task;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Product {

    public String certificate_document;
    public String certificate_document_date;
    public String certificate_document_number;
    public String owner_inn;
    public String producer_inn;
    public String production_date;
    public String tnved_code;
    public String uit_code;
    public String uitu_code;

    public static void main(String[] args) throws InterruptedException, IOException {
        CrptApiClient api = new CrptApiClient(10, TimeUnit.MINUTES); // Fixed constructor call

        Document document = new Document();
        document.description = new Description();
        document.description.participantInn = "string";
        document.doc_id = "string";
        document.doc_status = "string";
        document.doc_type = "LP_INTRODUCE_GOODS";
        document.importRequest = true;
        document.owner_inn = "string";
        document.participant_inn = "string";
        document.producer_inn = "string";
        document.production_date = "2020-01-23";
        document.production_type = "string";
        document.products = new Product[1];
        document.products[0] = new Product();
        document.products[0].certificate_document = "string";
        document.products[0].certificate_document_date = "2020-01-23";
        document.products[0].certificate_document_number = "string";
        document.products[0].owner_inn = "string";
        document.products[0].producer_inn = "string";
        document.products[0].production_date = "2020-01-23";
        document.products[0].tnved_code = "string";
        document.products[0].uit_code = "string";
        document.products[0].uitu_code = "string";
        document.reg_date = "2020-01-23";
        document.reg_number = "string";

        api.createDocument(document);
    }
}