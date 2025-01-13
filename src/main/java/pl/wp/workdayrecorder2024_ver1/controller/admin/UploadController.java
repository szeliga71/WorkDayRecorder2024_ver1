package pl.wp.workdayrecorder2024_ver1.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.wp.workdayrecorder2024_ver1.model.Employee;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UploadController {

    //private final String STORAGE = Paths.get("storage").toAbsolutePath().toString();
    private final String STORAGE = "src/main/resources/storage";

    public UploadController() {
        File directory = new File(STORAGE);
        if (!directory.exists()) directory.mkdirs();
    }

    @GetMapping("/admin/uploadWorkDayPlan")
    public String showUploadSite(@AuthenticationPrincipal Employee employee, Model model) {
        if (employee == null) {
            return "redirect:/login";
        }
        model.addAttribute("fullName", employee.getFirstName() + " " + employee.getLastName());
        return "admin/uploadWorkDayPlan";
    }

    @PostMapping("/admin/uploadWorkDayPlan")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            System.out.println("Received empty file");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }
        String fileName = file.getOriginalFilename();
        String fileExtension = "";
        if (fileName != null && fileName.contains(".")) {
            fileExtension = fileName.substring(fileName.lastIndexOf("."));
        }
//ten kod nadaje rozszerzenie z orginalnego pliku, teraz jest ustawione na sztywno jpg.
        try {
            Path path = Paths.get(STORAGE, "weekWorkPlan.jpg");/*+fileExtensionfile.getOriginalFilename());*/
            System.out.println("Attempting to save file to path: " + path.toString());
            Files.write(path, file.getBytes());
            System.out.println("File saved successfully");

            return ResponseEntity.ok("File uploaded successfully");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error uploading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        }
    }
}