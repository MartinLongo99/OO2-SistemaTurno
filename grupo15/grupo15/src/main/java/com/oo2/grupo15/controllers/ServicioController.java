package com.oo2.grupo15.controllers;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.oo2.grupo15.dtos.ServicioDTO;
import com.oo2.grupo15.services.IServicioService;
import com.oo2.grupo15.helpers.ViewRouteHelper;

@Controller
@RequestMapping("/servicio")
public class ServicioController {

    @Autowired
    private IServicioService servicioService;

    @GetMapping("/index")
    public String index(Model model) {
        List<ServicioDTO> servicios = servicioService.findAll();
        model.addAttribute("servicios", servicios);
        return ViewRouteHelper.SERVICIO_INDEX;
    }

    @GetMapping("/new")
    public String nuevo(Model model) {
        model.addAttribute("servicio", new ServicioDTO());
        model.addAttribute("diasSemana", Arrays.asList(DayOfWeek.values()));
        return ViewRouteHelper.SERVICIO_FORM;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("servicio") ServicioDTO dto,
                       @RequestParam(value = "diasSemana", required = false) List<String> diasSeleccionados) {

        Set<String> dias = diasSeleccionados != null
                ? diasSeleccionados.stream().map(String::toUpperCase).collect(Collectors.toSet())
                : Set.of();

        dto.setDiasSemana(dias);

        if (dto.getId() != null) {
            servicioService.update(dto.getId(), dto);
        } else {
            servicioService.save(dto);
        }

        return "redirect:/servicio/index";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Long id, Model model) {
        ServicioDTO servicio = servicioService.findById(id);
        model.addAttribute("servicio", servicio);
        model.addAttribute("diasSemana", Arrays.asList(DayOfWeek.values()));
        return ViewRouteHelper.SERVICIO_FORM;
    }

    @GetMapping("/delete/{id}")
    public String eliminar(@PathVariable Long id) {
        servicioService.delete(id);
        return "redirect:/servicio/index";
    }
}
