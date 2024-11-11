package pl.wp.workdayrecorder2024_ver1.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.model.Truck;
import pl.wp.workdayrecorder2024_ver1.service.TruckService;

@Controller
public class TruckListController {


    @Autowired
    TruckService truckService;

    @GetMapping("/admin/trucks")
    public String getTrucks(@AuthenticationPrincipal Employee employee, Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("trucks",truckService.getAllTrucks());
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/trucks";
    }




    @PostMapping("/admin/confirmDeletionTruck")
    public String confirmDeletionTruck(@AuthenticationPrincipal Employee loggedEmployee, @RequestParam("number") String number, Model model) {
        if (loggedEmployee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName());

        Truck truck=truckService.getTruckByTruckId(number);
        if (truck != null) {
            model.addAttribute("truck", truck);
            return "admin/confirmDeletionTruck";

        } else {
            model.addAttribute("error", "Truck with number: " + number + " not found.");
            return "redirect:/admin/trucks";
        }
    }

    @PostMapping("/admin/deleteTruck")
    public String deleteTruck(@AuthenticationPrincipal Employee loggedEmployee,@RequestParam("number") String number, Model model) {
        if (loggedEmployee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName());
        Truck truck=truckService.getTruckByTruckId(number);
        if (truck != null) {
            truckService.deleteTruck(number);
            model.addAttribute("truck", truck);
            model.addAttribute("message", "Truck with number: " + number + " has been removed.");
        } else {
            model.addAttribute("error", "Truck with number: " + number + " not found.");
        }
        return "admin/deleteTruck";
    }

    @GetMapping("/admin/confirmDeletionTruck")
    public String confirmDeletionTruck(@AuthenticationPrincipal Employee employee,Model model){
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/confirmDeletionTruck";

    }

    @GetMapping("/admin/deleteTruck")
    public String deleteTruck(@AuthenticationPrincipal Employee employee,Model model){
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/deleteTruck";
    }

    @GetMapping("/admin/addTruck")
    public String showTruckAddForm( @AuthenticationPrincipal Employee employee,Model model) {
        if (employee == null) {
            return "errorPage"; }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("truck",new Truck());
        return "admin/addTruck";
    }
    @PostMapping("/admin/saveTruck")
    public String saveTruck(@RequestParam("number") String number,
                              @RequestParam("notes") String notes,
                              Model model) {
        Truck truckExist = truckService.getTruckByTruckId(number);
        if (truckExist != null) {
            model.addAttribute("error", "Truck with number: " + number + " exist.");
            return "admin/addTruck";
        } else {
            Truck truck = new Truck();
            model.addAttribute("truck", truck);
            truck.setNumber(number);
            truck.setNotes(notes);
            truckService.saveTruck(truck);
            model.addAttribute("message", "Truck saved successfully");
        }
        return "redirect:/admin/trucks";
    }

    @GetMapping("/admin/updateTruck")
    public String updateTruck(@RequestParam("number") String number, Model model) {

        Truck truck=truckService.getTruckByTruckId(number);
        if (truck != null) {
            model.addAttribute("truck", truck);
            return "admin/updateTruck";
        } else {
            model.addAttribute("error", "Truck with number: " + number + " not found.");
            return "redirect:/admin/trucks";
        }
    }
    @PostMapping("/admin/updateTruck")
    public String updateTruck(@RequestParam("number") String number,
                                @RequestParam("notes") String notes,
                                Model model) {
        Truck truck=truckService.getTruckByTruckId(number);
        if (truck != null) {
            truck.setNotes(notes);
            truckService.saveTruck(truck);
            model.addAttribute("message", "Truck updated successfully");

        } else {

            model.addAttribute("error", "Truck with number: " + number + " not found.");
        }
        return "redirect:/admin/trucks";
    }

}
