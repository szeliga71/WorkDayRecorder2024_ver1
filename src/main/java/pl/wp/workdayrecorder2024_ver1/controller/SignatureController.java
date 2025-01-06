package pl.wp.workdayrecorder2024_ver1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import pl.wp.workdayrecorder2024_ver1.model.Employee;
import pl.wp.workdayrecorder2024_ver1.model.Signature;
import pl.wp.workdayrecorder2024_ver1.repository.EmployeeRepository;
import pl.wp.workdayrecorder2024_ver1.repository.SignatureRepository;
import java.util.Base64;


@Controller
public class SignatureController {

    @Autowired
    private SignatureRepository signatureRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/signature")
    public String getSignaturePage() {
        return "signature";
    }

    @PostMapping("/saveSignature")
    public ResponseEntity<String> saveSignature(@AuthenticationPrincipal Employee employee,
                                                @RequestBody SignatureRequest request) {

        try {
            // Dekodowanie Base64
            byte[] decodedBytes = Base64.getDecoder().decode(request.getImage().split(",")[1]);

            Signature signature = new Signature();
            signature.setImageData(decodedBytes);
            Signature savedSignature = signatureRepository.save(signature);
            Long signatureId = savedSignature.getId();

            employee.setSignatureId(signatureId);
            employeeRepository.save(employee);

            return ResponseEntity.ok("Unterschrift erfolgreich gespeichert");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Fehler beim Speichern der Unterschrift");
        }
    }
}

class SignatureRequest {
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}