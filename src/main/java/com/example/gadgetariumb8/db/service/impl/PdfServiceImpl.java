package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import com.example.gadgetariumb8.db.model.SubProduct;
import com.example.gadgetariumb8.db.repository.SubProductRepository;
import com.example.gadgetariumb8.db.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {
    private final SubProductRepository subProductRepository;
    private final TemplateEngine templateEngine;

    @Override
    public void downloadPdf(Long subProductId) {
        SubProduct subProduct = subProductRepository.findById(subProductId).orElseThrow(() -> new NotFoundException("Подпродукт с id: "+ subProductId + " не существует!!"));
        Context context = new Context();
        context.setVariable("brand", subProduct.getProduct().getBrand());
        context.setVariable("model", subProduct.getProduct().getName());
        context.setVariable("color", subProduct.getColour());
        context.setVariable("price", subProduct.getPrice());
        context.setVariable("discount", subProduct.getDiscount().getPercent());
        context.setVariable("date_of_issue", subProduct.getProduct().getDateOfIssue());
        context.setVariable("image", subProduct.getImages().stream().findAny().orElseThrow(() -> new NotFoundException("Найти изображение недействительно или не найдено!!")));
        String process = templateEngine.process("templates/pdf_template/download-pdf-template.html", context);
        System.out.println(process);
    }

    @Override
    public String exportPdf(Long subProductId) {
        return subProductRepository.findById(subProductId)
                .orElseThrow(() -> new NotFoundException("Подпродукт с id: "+subProductId+" не существует!!!"))
                .getProduct().getPDF();
    }

}
