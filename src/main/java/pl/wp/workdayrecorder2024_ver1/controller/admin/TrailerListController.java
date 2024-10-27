package pl.wp.workdayrecorder2024_ver1.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.model.Markt;
import pl.wp.workdayrecorder2024_ver1.model.Trailer;
import pl.wp.workdayrecorder2024_ver1.service.MarktService;
import pl.wp.workdayrecorder2024_ver1.service.TrailerService;

@Controller
public class TrailerListController {


    @Autowired
    TrailerService trailerService;

    @GetMapping("/admin/trailers")
    public String getTrailers(@AuthenticationPrincipal Employee employee, Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("trailers",trailerService.getAllTrailers() );
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/trailers";
    }
    @PostMapping("/admin/confirmDeletionTrailer")
    public String confirmDeletionTrailer(@AuthenticationPrincipal Employee loggedEmployee, @RequestParam("trailerId") String trailerId, Model model) {
        if (loggedEmployee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName());

        Trailer trailer=trailerService.getTrailerByTrailerId(trailerId);
        if (trailer != null) {
            model.addAttribute("trailer", trailer);
            return "admin/confirmDeletionTrailer";

        } else {
            model.addAttribute("error", "Trailer with id: " + trailerId + " not found.");
            return "redirect:/admin/trailers";
        }
    }

    @PostMapping("/admin/deleteTrailer")
    public String deleteTrailer(@AuthenticationPrincipal Employee loggedEmployee,@RequestParam("trailerId") String trailerId, Model model) {
        if (loggedEmployee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName());
        Trailer trailer=trailerService.getTrailerByTrailerId(trailerId);
        if (trailer != null) {
            trailerService.deleteTrailer(trailerId);
            model.addAttribute("trailer", trailer);
            model.addAttribute("message", "Trailer with id: " + trailerId + " has been removed.");
        } else {
            model.addAttribute("error", "Trailer with id: " + trailerId + " not found.");
        }
        return "admin/deleteTrailer";
    }

    @GetMapping("/admin/confirmDeletionTrailer")
    public String confirmDeletionTrailer(@AuthenticationPrincipal Employee employee,Model model){
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/confirmDeletionTrailer";

    }

    @GetMapping("/admin/deleteTrailer")
    public String deleteTrailer(@AuthenticationPrincipal Employee employee,Model model){
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/deleteTrailer";
    }

    @GetMapping("/admin/addTrailer")
    public String showTrailerAddForm( @AuthenticationPrincipal Employee employee,Model model) {
        if (employee == null) {
            return "errorPage"; }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        model.addAttribute("trailer",new Trailer());
        return "admin/addTrailer";
    }
    @PostMapping("/admin/saveTrailer")
    public String updateTrailer(@RequestParam("trailerId") String trailerId,
                              Model model) {
        Trailer trailer=trailerService.getTrailerByTrailerId(trailerId);
        if (trailer != null) {
            model.addAttribute("trailer" ,trailer);
            trailer.setNumber(trailerId);

            trailerService.saveTrailer(trailer);
            model.addAttribute("message", "Trailer updated successfully");

        } else {
            model.addAttribute("error", "Trailer with id: " + trailerId + " not found.");
        }
        return "redirect:/admin/trailers";
    }

    @GetMapping("/admin/updateTrailer")
    public String showUpdateTrailerForm(@RequestParam("trailerId") String trailerId, Model model) {
        Trailer trailer=trailerService.getTrailerByTrailerId(trailerId);
        if (trailer != null) {
            model.addAttribute("trailer",trailer);
            return "admin/updateTrailer";
        } else {
            model.addAttribute("error", "Trailer with id: " + trailerId + " not found.");
            return "redirect:/admin/trailers";
        }
    }
}