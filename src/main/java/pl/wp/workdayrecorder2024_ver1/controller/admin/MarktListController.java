package pl.wp.workdayrecorder2024_ver1.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.model.Markt;
import pl.wp.workdayrecorder2024_ver1.service.MarktService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class MarktListController {

    @Autowired
    MarktService marktService;

    @GetMapping("/markets")
    public String getMarkts(@AuthenticationPrincipal Employee employee,
                            @RequestParam(defaultValue = "marktId") String sortField,
                            @RequestParam(defaultValue = "asc") String sortDir,
                            @RequestParam(defaultValue = "0") int page,
                            Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Page<Markt> marktsPage = marktService.getAllMarkts(PageRequest.of(page, 1000, sort));
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("markets", marktsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", marktsPage.getTotalPages());
        model.addAttribute("totalItems", marktsPage.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");
        return "admin/markets";
    }

    @PostMapping("/confirmDeletionMarkt")
    public String confirmDeletionMarkt(@AuthenticationPrincipal Employee loggedEmployee, @RequestParam("marktId") String marktId, Model model) {
        if (loggedEmployee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName());
        Markt markt = marktService.getMarktByMarktId(marktId);
        if (markt != null) {
            model.addAttribute("markt", markt);
            return "admin/confirmDeletionMarkt";
        } else {
            model.addAttribute("error", "Markt with id: " + marktId + " not found.");
            return "redirect:/admin/markets";
        }
    }

    @PostMapping("/deleteMarkt")
    public String deleteMarkt(@AuthenticationPrincipal Employee loggedEmployee, @RequestParam("marktId") String marktId, Model model) {
        if (loggedEmployee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName());
        Markt markt = marktService.getMarktByMarktId(marktId);
        if (markt != null) {
            marktService.deleteMarkt(markt);
            model.addAttribute("markt", markt);
            model.addAttribute("message", "Markt with id: " + marktId + " has been removed.");
        } else {
            model.addAttribute("error", "Markt with id: " + marktId + " not found.");
        }
        return "admin/deleteMarkt";
    }

    @GetMapping("/confirmDeletionMarkt")
    public String confirmDeletionMarkt(@AuthenticationPrincipal Employee employee, Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/confirmDeletionMarkt";
    }

    @GetMapping("/deleteMarkt")
    public String deleteMarkt(@AuthenticationPrincipal Employee employee, Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/deleteMarkt";
    }

    @GetMapping("/addMarket")
    public String showMarktForm(@AuthenticationPrincipal Employee employee, Model model) {
        if (employee == null) {
            return "error";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("markt", new Markt());
        return "admin/addMarket";
    }

    @PostMapping("/saveMarkt")
    public String saveMarkt(@RequestParam("marktId") String marktId,
                            @RequestParam("name") String name,
                            @RequestParam("postalCode") String postalCode,
                            @RequestParam("citi") String citi,
                            @RequestParam("street") String street,
                            @RequestParam("buildingNumber") String buildingNumber,
                            @RequestParam("notes") String notes,
                            Model model) {
        Markt marktE = marktService.getMarktByMarktId(marktId);
        if (marktE != null) {
            model.addAttribute("error", "Markt with id: " + marktId + " exist.");
            return "admin/addMarket";
        } else {
            Markt markt = new Markt();
            model.addAttribute("markt", markt);
            markt.setMarktId(marktId);
            markt.setName(name);
            markt.setPostalCode(postalCode);
            markt.setCiti(citi);
            markt.setStreet(street);
            markt.setBuildingNumber(buildingNumber);
            markt.setNotes(notes);
            marktService.saveMarkt(markt);
            model.addAttribute("message", "Markt saved successfully");
            return "redirect:/admin/markets";
        }
    }

    @PostMapping("/updateMarkt")
    public String updateMarkt(@RequestParam("marktId") String marktId,
                              @RequestParam("name") String name,
                              @RequestParam("postalCode") String postalCode,
                              @RequestParam("citi") String citi,
                              @RequestParam("street") String street,
                              @RequestParam("buildingNumber") String buildingNumber,
                              @RequestParam("notes") String notes,
                              Model model) {
        Markt markt = marktService.getMarktByMarktId(marktId);
        if (markt != null) {
            markt.setName(name);
            markt.setPostalCode(postalCode);
            markt.setCiti(citi);
            markt.setStreet(street);
            markt.setBuildingNumber(buildingNumber);
            markt.setNotes(notes);
            marktService.saveMarkt(markt);
            model.addAttribute("message", "Markt updated successfully");
        } else {
            model.addAttribute("error", "Employee with id: " + marktId + " not found.");
        }
        return "redirect:/admin/markets";
    }

    @GetMapping("/updateMarkt")
    public String showUpdateMarktForm(@RequestParam("marktId") String marktId, Model model) {
        Markt markt = marktService.getMarktByMarktId(marktId);
        if (markt != null) {
            model.addAttribute("markt", markt);
            return "admin/updateMarkt";
        } else {
            model.addAttribute("error", "Markt with id: " + marktId + " not found.");
            return "redirect:/admin/markets";
        }
    }

    @GetMapping("/getMarkets")
    @ResponseBody
    public List<Markt> getAllMarkets() {
        List<Markt> markets = marktService.getAllMarkts();
        markets.forEach(markt -> System.out.println(markt.getMarktId() + ": " + markt.getName()));
        return markets;
    }
}