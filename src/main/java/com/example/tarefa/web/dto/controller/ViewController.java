package com.example.tarefa.web.dto.controller;

import com.example.tarefa.entity.Tarefa;
import com.example.tarefa.service.TarefaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final TarefaService tarefaService;

    @GetMapping("/create")
    public String showCreateForm() {
        return "create";
    }

    @GetMapping("/edit")
    public String showEditForm() {
        return "edit";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataInicial,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataFinal,

            @RequestParam(required = false)
            String situacao,

            Model model
    ) {
        List<Tarefa> tarefas = tarefaService.findByFiltro(dataInicial, dataFinal, situacao);
        model.addAttribute("tarefas", tarefas);
        model.addAttribute("dataInicial", dataInicial);
        model.addAttribute("dataFinal", dataFinal);
        model.addAttribute("situacao", situacao);
        return "index";
    }
}
