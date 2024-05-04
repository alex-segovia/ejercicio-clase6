package com.example.clase6gtics.controller;

import com.example.clase6gtics.entity.Employee;
import com.example.clase6gtics.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping(value = {"", "/", "list"})
    public String listarEmpleados(Model model) {
        model.addAttribute("listaEmpleados", employeeRepository.findAll());
        model.addAttribute("listaEmpleadosPorRegion", employeeRepository.obtenerEmpleadosPorRegion());
        model.addAttribute("listaEmpleadosPorPais", employeeRepository.obtenerEmpleadosPorPais());
        return "employee/list";
    }

    @GetMapping("/new")
    public String nuevoEmpleadoFrm(Model model, @ModelAttribute("employee") Employee employee) {
        model.addAttribute("listaJefes", getListaJefes());
        model.addAttribute("tipo",false);
        return "employee/newFrm";
    }

    public List<Employee> getListaJefes() {
        List<Employee> listaJefes = employeeRepository.findAll();
        Employee e = new Employee();
        e.setId(0);
        e.setFirstname("--No tiene Jefe--");
        listaJefes.add(0, e);
        return listaJefes;
    }

    @PostMapping("/save")
    public String guardarEmpleado(RedirectAttributes attr, @ModelAttribute("employee") Employee employee) {
        if (employee.getId() == 0) {
            attr.addFlashAttribute("msg", "Empleado creado exitosamente");
        } else {
            attr.addFlashAttribute("msg", "Empleado actualizado exitosamente");
        }
        employeeRepository.save(employee);
        return "redirect:/employee";
    }

    @GetMapping("/edit")
    public String editarEmpleado(@ModelAttribute("employee") Employee employee, Model model, @RequestParam("id") int id) {
        Optional<Employee> optEmployee = employeeRepository.findById(id);

        if (optEmployee.isPresent()) {
            employee = optEmployee.get();
            model.addAttribute("employee", employee);
            model.addAttribute("listaJefes", getListaJefes());
            model.addAttribute("tipo",true);
            return "employee/newFrm";
        } else {
            return "redirect:/employee";
        }

    }

    @GetMapping("/delete")
    public String borrarEmpleado(@RequestParam("id") int id, RedirectAttributes attr) {
        Optional<Employee> optional = employeeRepository.findById(id);

        if (optional.isPresent()) {
            employeeRepository.deleteById(id);
        }
        attr.addFlashAttribute("msg", "usuario borrado exitosamente");
        return "redirect:/employee";
    }
}
