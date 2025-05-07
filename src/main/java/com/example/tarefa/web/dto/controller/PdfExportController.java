package com.example.tarefa.web.dto.controller;

import com.example.tarefa.entity.Tarefa;
import com.example.tarefa.service.TarefaService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PdfExportController {

    private final TarefaService tarefaService;

    @GetMapping(value = "/tarefas/v1/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportPdf(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataInicial,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataFinal,

            @RequestParam(required = false)
            String situacao,

            HttpServletResponse response
    ) throws DocumentException, IOException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"tarefas.pdf\"");

        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();

        Font fTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLUE);
        doc.add(new Paragraph("Lista de Tarefas", fTitle));
        doc.add(Chunk.NEWLINE);

        LocalDateTime inicio = dataInicial != null ? dataInicial.atStartOfDay() : null;
        LocalDateTime fim    = dataFinal   != null ? dataFinal.atTime(LocalTime.MAX) : null;

        List<Tarefa> tarefas = tarefaService.findByFiltro(dataInicial, dataFinal, situacao);

        for (Tarefa t : tarefas) {
            doc.add(new Paragraph("ID: " + t.getId()));
            doc.add(new Paragraph("Criada em: " +
                    (t.getDataCriacao() != null
                            ? t.getDataCriacao().toString()
                            : "-")));
            doc.add(new Paragraph("Descrição: " + t.getDescricao()));
            doc.add(new Paragraph("Prevista: " +
                    (t.getDataPrevista() != null
                            ? t.getDataPrevista().toString()
                            : "-")));
            doc.add(new Paragraph("Encerramento: " +
                    (t.getDataEncerramento() != null
                            ? t.getDataEncerramento().toString()
                            : "-")));
            doc.add(new Paragraph("Situação: " + t.getSituacao()));
            doc.add(Chunk.NEWLINE);
        }

        doc.close();
    }
}
