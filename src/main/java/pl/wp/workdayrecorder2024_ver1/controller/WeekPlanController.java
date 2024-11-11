package pl.wp.workdayrecorder2024_ver1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import pl.wp.workdayrecorder2024_ver1.model.Employee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
public class WeekPlanController {

    private final Path storagePath = Paths.get("storage").toAbsolutePath();

    @GetMapping("/viewWeekWorkPlan")
    public String showWeekPlan(@AuthenticationPrincipal Employee employee, Model model) {
        model.addAttribute("role", employee.getRole());
        return "viewWeekWorkPlan";
    }

    @GetMapping("/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> displayFile(@PathVariable String filename) {
        try {
            Path filePath = storagePath.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                // Ustawienie odpowiednich nagłówków
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"");
                headers.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath)); // Wykrywanie typu MIME
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
