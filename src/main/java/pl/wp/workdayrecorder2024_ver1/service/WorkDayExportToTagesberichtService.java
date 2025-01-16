package pl.wp.workdayrecorder2024_ver1.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wp.workdayrecorder2024_ver1.model.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pl.wp.workdayrecorder2024_ver1.repository.SignatureRepository;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

@Service
public class WorkDayExportToTagesberichtService {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    SignatureRepository signatureRepository;

    String filePath = "excellFiles/excellFormular/tagebericht.xlsx";

    public WorkDayExportToTagesberichtService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

   public void exportToTagesbericht(WorkDay workDay) {
       String personalId = workDay.getPersonalId();
       Employee employee = employeeService.findEmployeeByPersonalId(personalId);
       Long signatureId = employee.getSignatureId();

       try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
           if (inputStream == null) {
               throw new FileNotFoundException("Resource not found: excellFiles/excellFormular/tagebericht.xlsx");
           }

           Workbook workbook = new XSSFWorkbook(inputStream);
           Sheet sheet = workbook.getSheetAt(0);

           setCellValue(sheet, 6, 1, employee.getFirstName() + " " + employee.getLastName());
           setCellValue(sheet, 6, 5, personalId);
           setCellValue(sheet, 5, 11, workDay.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

           setCellValue(sheet, 7, dayOfWeekInFormular(workDay.getDayOfWeek()), "XXXXXXX");

           setCellValue(sheet, 6, 17, workDay.getStartOfWork().format(DateTimeFormatter.ofPattern("HH:mm")));
           setCellValue(sheet, 6, 19, workDay.getPause());
           setCellValue(sheet, 6, 21, workDay.getEndOfWork().format(DateTimeFormatter.ofPattern("HH:mm")));

           setCellValue(sheet, 39, 7, workDay.getTotalDistance());

           setCellValue(sheet, 41, fahrerbruchInExcell(workDay.isFaults()), "X");
           setCellValue(sheet, 41, unfallInExcell(workDay.isAccident()), "X");

           signatureRepository.findById(signatureId).ifPresent(signature -> {
               try {
                   int pictureIndex = workbook.addPicture(signature.getImageData(), Workbook.PICTURE_TYPE_PNG);
                   XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
                   ClientAnchor anchor = new XSSFClientAnchor();
                   anchor.setCol1(20);
                   anchor.setRow1(37);
                   XSSFPicture picture = drawing.createPicture(anchor, pictureIndex);
                   picture.resize(0.15);
               } catch (Exception e) {
                   e.printStackTrace();
               }
           });

           addRoutesAndStops(sheet, workDay);

           saveWorkbook(workbook, workDay);

       } catch (IOException e) {
           e.printStackTrace();
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
   }

    private void setCellValue(Sheet sheet, int rowIndex, int colIndex, Object value) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) row = sheet.createRow(rowIndex);
        Cell cell = row.getCell(colIndex);
        if (cell == null) cell = row.createCell(colIndex);
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        }
    }

    private void addRoutesAndStops(Sheet sheet, WorkDay workDay) {
        int next = 0;
        int stopInNextTour = 0;

        for (Route route : workDay.getRoutes()) {

            setCellValue(sheet, 9 + next, 5, route.getRouteNumber());
            setCellValue(sheet, 11 + next, 1, route.getTruckNumber());
            setCellValue(sheet, 11 + next, 2, route.getTrailerNumber() != null ? route.getTrailerNumber() : "---");
            setCellValue(sheet, 11 + next, 3, route.getDistance());

            setCellValue(sheet, 15 + next, 1, route.getStartOfRoute().format(DateTimeFormatter.ofPattern("HH:mm")));
            setCellValue(sheet, 15 + next, 3, route.getDepartureFromTheBase().format(DateTimeFormatter.ofPattern("HH:mm")));
            setCellValue(sheet, 17 + next, 1, route.getArrivalToTheBase().format(DateTimeFormatter.ofPattern("HH:mm")));
            setCellValue(sheet, 17 + next, 3, route.getEndOfRoute().format(DateTimeFormatter.ofPattern("HH:mm")));
            setCellValue(sheet, 10 + next, 17, route.getNotes());

            addStops(sheet, route, stopInNextTour);
            next += 10;
        }
    }

    private void addStops(Sheet sheet, Route route, int stopInNextTour) {
        int nextStopCell = 0;
        int rightStopCell = 0;

        for (Stop stop : route.getStops()) {
            setCellValue(sheet, 11 + nextStopCell + stopInNextTour, 5 + rightStopCell, stop.getMarktId());
            setCellValue(sheet, 11 + nextStopCell + stopInNextTour, 7 + rightStopCell, stop.getBeginn().format(DateTimeFormatter.ofPattern("HH:mm")));
            setCellValue(sheet, 11 + nextStopCell + stopInNextTour, 9 + rightStopCell, stop.getEndOfStopp().format(DateTimeFormatter.ofPattern("HH:mm")));

            nextStopCell = (nextStopCell < 8) ? nextStopCell + 1 : 0;
            if (nextStopCell == 0) rightStopCell += 6;
        }
    }

    private void saveWorkbook(Workbook workbook, WorkDay workDay) throws IOException {
        String serverDirectory = "/tmp/tagesberichte";
        String fileName = "tagebericht-" + workDay.getDate() + "-" + workDay.getPersonalId() + ".xlsx";

        Path path = Paths.get(serverDirectory);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        try (FileOutputStream fos = new FileOutputStream(serverDirectory + "/" + fileName)) {
            workbook.write(fos);
            System.out.println("Data successfully saved on the file: " + fileName);
        }
    }


    public int dayOfWeekInFormular(String dayOfWeek) {
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("dayOfWeek cannot be null");
        }
        switch (dayOfWeek.toLowerCase()) {
            case "monday":
                return 10;
            case "tuesday":
                return 11;
            case "wednesday":
                return 12;
            case "thursday":
                return 13;
            case "friday":
                return 14;
            case "saturday":
                return 15;
            case "sunday":
                return 16;
            default:
                throw new IllegalArgumentException("Invalid day of the week: " + dayOfWeek);
        }
    }
    public int fahrerbruchInExcell(boolean isBruch) {
        return (isBruch) ? 5 : 6;
    }
    public int unfallInExcell(boolean isUnfall) {
        return (isUnfall) ? 9 : 10;
    }
}