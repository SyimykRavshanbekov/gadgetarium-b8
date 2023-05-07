package com.example.gadgetariumb8.db.service;

public interface PdfService {
    void downloadPdf(Long subProductId);

    String exportPdf(Long subProductId);
}
