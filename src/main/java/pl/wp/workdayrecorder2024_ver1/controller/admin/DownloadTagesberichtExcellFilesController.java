package pl.wp.workdayrecorder2024_ver1.controller.admin;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class DownloadTagesberichtExcellFilesController {

    private final String FILE_DIRECTORY = "/tmp/tagesberichte";

    @GetMapping("/admin/downloadTagesberichtExcellFiles")
    public String listFiles(Model model) throws IOException {
        Path folderPath = Paths.get(FILE_DIRECTORY);
        var files = Files.list(folderPath)
                .filter(Files::isRegularFile)
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toList());

        model.addAttribute("files", files);
        return "admin/downloadTagesberichtExcellFiles";
    }
    @GetMapping("/downloadAll")
    public ResponseEntity<ByteArrayResource> downloadAllFiles(RedirectAttributes redirectAttributes) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            Path folderPath = Paths.get(FILE_DIRECTORY);
            List<Path> files = Files.list(folderPath).filter(Files::isRegularFile).toList();

            for (Path file : files) {
                zos.putNextEntry(new ZipEntry(file.getFileName().toString()));
                byte[] fileContent = Files.readAllBytes(file);
                zos.write(fileContent);
                zos.closeEntry();
            }
            zos.finish();
            for (Path file : files) {
                Files.delete(file);
            }
            redirectAttributes.addFlashAttribute("message", "Wszystkie pliki zostały pobrane i usunięte z katalogu źródłowego.");
            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"all_files.zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}