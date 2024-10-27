package pl.wp.workdayrecorder2024_ver1.controller.admin;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.model.Markt;
import pl.wp.workdayrecorder2024_ver1.service.MarktService;

@Controller
public class MarktListController {

    @Autowired
    MarktService marktService;

    @GetMapping("/admin/markets")
    public String getMarkts(@AuthenticationPrincipal Employee employee, Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("markets", marktService.getAllMarkts());
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/markets";
    }
    @PostMapping("/admin/confirmDeletionMarkt")
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

    @PostMapping("/admin/deleteMarkt")
    public String deleteMarkt(@AuthenticationPrincipal Employee loggedEmployee,@RequestParam("marktId") String marktId, Model model) {
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

    @GetMapping("/admin/confirmDeletionMarkt")
    public String confirmDeletionMarkt(@AuthenticationPrincipal Employee employee,Model model){
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/confirmDeletionMarkt";

    }

    @GetMapping("/admin/deleteMarkt")
    public String deleteMarkt(@AuthenticationPrincipal Employee employee,Model model){
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/deleteMarkt";
    }

    @GetMapping("/admin/addMarket")
    public String showMarktForm( @AuthenticationPrincipal Employee employee,Model model) {
            if (employee == null) {
                return "errorPage"; }
            model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
      model.addAttribute("markt",new Markt());
        return "admin/addMarket";
    }
    @PostMapping("/admin/saveMarkt")
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
            model.addAttribute("markt", markt);

            markt.setMarktId(marktId);
            markt.setName(name);
            markt.setPostalCode(postalCode);
            markt.setCiti(citi);
            markt.setStreet(street);
            markt.setBuildingNumber(buildingNumber);
            markt.setNotes(notes);

            marktService.saveMarkt(markt);
            model.addAttribute("message", "Markt updated successfully");

        } else {
            model.addAttribute("error", "Markt with id: " + marktId + " not found.");
        }
            return "redirect:/admin/markets";
        }

    @GetMapping("/admin/updateMarkt")
    public String showUpdateMarktForm(@RequestParam("marktId") String marktId, Model model) {
    Markt markt=marktService.getMarktByMarktId(marktId);
         if (markt != null) {
            model.addAttribute("markt", markt);
            return "admin/updateMarkt";
       } else {
            model.addAttribute("error", "Markt with id: " + marktId + " not found.");
            return "redirect:/admin/markets";
        }
    }
}