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
import java.util.Optional;

@Service
public class WorkDayExportToTagesberichtService {


    private final EmployeeService employeeService;
    //String filePath = "src/main/resources/excellFiles/excellFormular/tagebericht.xlsx";


    @Autowired
    SignatureRepository signatureRepository;


    public WorkDayExportToTagesberichtService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public void exportToTagesbericht(WorkDay workDay) {

        String personalId = workDay.getPersonalId();
        Employee employee = employeeService.findEmployeeByPersonalId(personalId);
        Long signatureId = employee.getSignatureId();
        String name = employee.getFirstName();
        String lastName = employee.getLastName();


        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("excellFiles/excellFormular/tagebericht.xlsx")) {
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: excellFiles/excellFormular/tagebericht.xlsx");
            }
       // try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(inputStream);

            // Pobierz istniejący arkusz (pierwszy arkusz)
            Sheet sheet = workbook.getSheetAt(0);


            //Name Vorname
            Row rowB7 = sheet.getRow(6);
            if (rowB7 == null) {
                rowB7 = sheet.createRow(6);
            }
            Cell cellB7 = rowB7.getCell(1);
            if (cellB7 == null) {
                cellB7 = rowB7.createCell(1);
            }
            cellB7.setCellValue(name + " " + lastName);

            // Zapisz personalId do komórki F7 (wiersz 6, kolumna 5, indeksy od 0)
            Row rowF7 = sheet.getRow(6);
            if (rowF7 == null) {
                rowF7 = sheet.createRow(6);
            }
            Cell cellF7 = rowF7.getCell(5);
            if (cellF7 == null) {
                cellF7 = rowF7.createCell(5);
            }
            cellF7.setCellValue(workDay.getPersonalId());

            //date
            Row rowL6 = sheet.getRow(5);
            if (rowL6 == null) {
                rowL6 = sheet.createRow(5);
            }
            Cell cellL6 = rowL6.getCell(11);
            if (cellL6 == null) {
                cellL6 = rowL6.createCell(11);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formatedDate = workDay.getDate().format(formatter);
            cellL6.setCellValue(formatedDate);

            //dzien tygodnia
            int cellDayOfWeekNumber = dayOfWeekInFormular(workDay.getDayOfWeek());
            Row rowC6 = sheet.getRow(7);
            if (rowC6 == null) {
                rowC6 = sheet.createRow(7);
            }
            Cell cellC6 = rowC6.getCell(cellDayOfWeekNumber);
            if (cellC6 == null) {
                cellC6 = rowC6.createCell(cellDayOfWeekNumber);
            }
            cellC6.setCellValue("XXXXXXX");

            //arbeitsbeginn
            Row rowR7 = sheet.getRow(6);
            if (rowR7 == null) {
                rowR7 = sheet.createRow(6);
            }
            Cell cellR7 = rowR7.getCell(17);
            if (cellR7 == null) {
                cellR7 = rowR7.createCell(17);
            }
            DateTimeFormatter formatterTimeBegin = DateTimeFormatter.ofPattern("HH:mm");
            String formatedTimeBegin = workDay.getStartOfWork().format(formatterTimeBegin);
            cellR7.setCellValue(formatedTimeBegin);

            //Pause
            Row rowT7 = sheet.getRow(6);
            if (rowT7 == null) {
                rowT7 = sheet.createRow(6);
            }
            Cell cellT7 = rowT7.getCell(19);
            if (cellT7 == null) {
                cellT7 = rowT7.createCell(19);
            }
            cellT7.setCellValue(workDay.getPause());

            //Arbeitsende
            Row rowV7 = sheet.getRow(6);
            if (rowV7 == null) {
                rowV7 = sheet.createRow(6);
            }
            Cell cellV7 = rowV7.getCell(21);
            if (cellV7 == null) {
                cellV7 = rowV7.createCell(21);
            }
            DateTimeFormatter formatterTimeEnd = DateTimeFormatter.ofPattern("HH:mm");
            String formatedTimeEnd = workDay.getStartOfWork().format(formatterTimeEnd);
            cellV7.setCellValue(formatedTimeEnd);

            Row rowH40 = sheet.getRow(39);
            if (rowH40 == null) {
                rowH40 = sheet.createRow(39);
            }
            Cell cellH40 = rowH40.getCell(7);
            if (cellH40 == null) {
                cellH40 = rowH40.createCell(7);
            }
            cellH40.setCellValue(workDay.getTotalDistance());

            //Fuhrerbruch
            int fahrerbruchCell = fahrerbruchInExcell(workDay.isFaults());
            Row rowF42 = sheet.getRow(41);
            if (rowF42 == null) {
                rowF42 = sheet.createRow(41);
            }
            Cell cellF42 = rowF42.getCell(fahrerbruchCell);
            if (cellF42 == null) {
                cellF42 = rowF42.createCell(fahrerbruchCell);
            }
            cellF42.setCellValue("X");

            //unfall
            int unfallCell = unfallInExcell(workDay.isAccident());
            Row rowJ42 = sheet.getRow(41);
            if (rowJ42 == null) {
                rowJ42 = sheet.createRow(41);
            }
            Cell cellJ42 = rowJ42.getCell(unfallCell);
            if (cellJ42 == null) {
                cellJ42 = rowJ42.createCell(unfallCell);
            }
            cellJ42.setCellValue("X");

            //untrschrift Fahrer
            Optional<Signature> optionalSignature = signatureRepository.findById(signatureId);
            if (optionalSignature.isPresent()) {
                byte[] imageData = optionalSignature.get().getImageData();

                int pictureIndex = workbook.addPicture(imageData, Workbook.PICTURE_TYPE_PNG);
                XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
                ClientAnchor anchor = new XSSFClientAnchor();
                anchor.setCol1(20);
                anchor.setRow1(39);
                XSSFPicture picture = drawing.createPicture(anchor, pictureIndex);
                picture.resize(0.15);
            }

//============================================================================
//lista route

            int stopInNextTour = 0;
            int numberOfRoutes = workDay.getRoutes().size();
            int next = 0;
            if (numberOfRoutes < 4 && workDay.getRoutes() != null && !workDay.getRoutes().isEmpty()) {

                for (Route route : workDay.getRoutes()) {

                    Row rowF10 = sheet.getRow(9 + next);
                    if (rowF10 == null) {
                        rowF10 = sheet.createRow(9 + next);
                    }
                    Cell cellF10 = rowF10.getCell(5);
                    if (cellF10 == null) {
                        cellF10 = rowF10.createCell(5);
                    }
                    cellF10.setCellValue(route.getRouteNumber());
                    //LKW
                    Row rowB12 = sheet.getRow(11 + next);
                    if (rowB12 == null) {
                        rowB12 = sheet.createRow(11 + next);
                    }
                    Cell cellB12 = rowB12.getCell(1);
                    if (cellB12 == null) {
                        cellB12 = rowB12.createCell(1);
                    }
                    cellB12.setCellValue(route.getTruckNumber());
                    //Trailer
                    Row rowC12 = sheet.getRow(11 + next);
                    if (rowC12 == null) {
                        rowC12 = sheet.createRow(11 + next);
                    }
                    Cell cellC12 = rowC12.getCell(2);
                    if (cellC12 == null) {
                        cellC12 = rowC12.createCell(2);
                    }
                    if (route.getTrailerNumber() == null || route.getTrailerNumber().isEmpty()) {
                        cellC12.setCellValue("---");
                    } else {
                        cellC12.setCellValue(route.getTrailerNumber());
                    }
                    // distance
                    Row rowD12 = sheet.getRow(11 + next);
                    if (rowD12 == null) {
                        rowD12 = sheet.createRow(11 + next);
                    }
                    Cell cellD12 = rowD12.getCell(3);
                    if (cellD12 == null) {
                        cellD12 = rowD12.createCell(3);
                    }
                    cellD12.setCellValue(route.getDistance());


                    //Tourbeginn
                    Row rowB16 = sheet.getRow(15 + next);
                    if (rowB16 == null) {
                        rowB16 = sheet.createRow(15 + next);
                    }
                    Cell cellB16 = rowB16.getCell(1);
                    if (cellB16 == null) {
                        cellB16 = rowB16.createCell(1);
                    }
                    DateTimeFormatter formatterTourBegin = DateTimeFormatter.ofPattern("HH:mm");
                    String formatedTourBegin = route.getStartOfRoute().format(formatterTourBegin);
                    cellB16.setCellValue(formatedTourBegin);

                    //Abfahrt Lager
                    Row rowD16 = sheet.getRow(15 + next);
                    if (rowD16 == null) {
                        rowD16 = sheet.createRow(15 + next);
                    }
                    Cell cellD16 = rowD16.getCell(3);
                    if (cellD16 == null) {
                        cellD16 = rowD16.createCell(3);
                    }

                    DateTimeFormatter formatterAbfahrtLager = DateTimeFormatter.ofPattern("HH:mm");
                    String formattedAbfahrtLager = route.getDepartureFromTheBase().format(formatterAbfahrtLager);
                    cellD16.setCellValue(formattedAbfahrtLager);


                    //Ankunft Lager
                    Row rowB18 = sheet.getRow(17 + next);
                    if (rowB18 == null) {
                        rowB18 = sheet.createRow(17 + next);
                    }
                    Cell cellB18 = rowB18.getCell(1);
                    if (cellB18 == null) {
                        cellB18 = rowB18.createCell(1);
                    }
                    DateTimeFormatter formatterAnkuftLager = DateTimeFormatter.ofPattern("HH:mm");
                    String formatedAnkunftLager = route.getArrivalToTheBase().format(formatterAnkuftLager);
                    cellB18.setCellValue(formatedAnkunftLager);

                    //Tourende
                    Row rowD18 = sheet.getRow(17 + next);
                    if (rowD18 == null) {
                        rowD18 = sheet.createRow(17 + next);
                    }
                    Cell cellD18 = rowD18.getCell(3);
                    if (cellD18 == null) {
                        cellD18 = rowD18.createCell(3);
                    }
                    DateTimeFormatter formatterTourenende = DateTimeFormatter.ofPattern("HH:mm");
                    String formattedTourenende = route.getEndOfRoute().format(formatterTourenende);
                    cellD18.setCellValue(formattedTourenende);

                    //notes
                    Row rowR11 = sheet.getRow(10 + next);
                    if (rowR11 == null) {
                        rowR11 = sheet.createRow(10 + next);
                    }
                    Cell cellR11 = rowR11.getCell(17);
                    if (cellR11 == null) {
                        cellR11 = rowR11.createCell(17);
                    }
                    cellR11.setCellValue(route.getNotes());

                    next = next + 10;


                    //=====================================================
                    //STOP
                    int numberOfStops = route.getStops().size();
                    int nextStopCell = 0;
                    int rightStopCell = 0;
                    if (numberOfStops < 15 && route.getStops() != null && !route.getStops().isEmpty()) {

                        for (Stop stop : route.getStops()) {
                            //markt nummer
                            Row rowF12 = sheet.getRow(11 + nextStopCell + stopInNextTour);
                            if (rowF12 == null) {
                                rowF12 = sheet.createRow(11 + nextStopCell + stopInNextTour);
                            }
                            Cell cellF12 = rowF12.getCell(5 + rightStopCell);
                            if (cellF12 == null) {
                                cellF12 = rowF12.createCell(5 + rightStopCell);
                            }
                            cellF12.setCellValue(stop.getMarktId());

                            //stopbeginn
                            Row rowH12 = sheet.getRow(11 + nextStopCell + stopInNextTour);
                            if (rowH12 == null) {
                                rowH12 = sheet.createRow(11 + nextStopCell + stopInNextTour);
                            }
                            Cell cellH12 = rowH12.getCell(7 + rightStopCell);
                            if (cellH12 == null) {
                                cellH12 = rowH12.createCell(7 + rightStopCell);
                            }
                            DateTimeFormatter formatterStopBegin = DateTimeFormatter.ofPattern("HH:mm");
                            String formatedStopBegin = stop.getBeginn().format(formatterStopBegin);
                            cellH12.setCellValue(formatedStopBegin);

                            //stopende

                            Row rowJ12 = sheet.getRow(11 + nextStopCell + stopInNextTour);
                            if (rowJ12 == null) {
                                rowJ12 = sheet.createRow(11 + nextStopCell + stopInNextTour);
                            }
                            Cell cellJ12 = rowJ12.getCell(9 + rightStopCell);
                            if (cellJ12 == null) {
                                cellJ12 = rowJ12.createCell(9 + rightStopCell);
                            }
                            DateTimeFormatter formatterStopEnde = DateTimeFormatter.ofPattern("HH:mm");
                            String formatedStopEnde = stop.getEndOfStopp().format(formatterStopEnde);
                            cellJ12.setCellValue(formatedStopEnde);


                            if (rightStopCell < 15) {
                                nextStopCell = (nextStopCell < 8) ? nextStopCell + 1 : 0;
                                if (nextStopCell == 0) {
                                    rightStopCell += 6;
                                }
                            } else {
                                throw new Exception("Wartość rightStopCell przekroczyła 15.");
                            }
                        }
                    }
                    stopInNextTour = stopInNextTour + 10;
                }
            }

            /*String outFilePath = "C:/Tagesbericht/tagebericht" + workDay.getDate() + "-" + workDay.getPersonalId() + ".xlsx";
            try (FileOutputStream fos = new FileOutputStream(outFilePath)) {
                workbook.write(fos);
                System.out.println("Dane zapisane pomyślnie do pliku Excel.");
            }*/
            //String serverDirectory = "/var/app/reports/tagesberichte";
            String serverDirectory = "/tmp/tagesberichte";

            String fileName = "tagebericht-" + workDay.getDate() + "-" + workDay.getPersonalId() + ".xlsx";

            try {
                Path path = Paths.get(serverDirectory);
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                    System.out.println("Katalog został utworzony: " + serverDirectory);
                } else {
                    System.out.println("Katalog już istnieje: " + serverDirectory);
                }
            } catch (IOException e) {
                System.err.println("Błąd podczas tworzenia katalogu: " + serverDirectory);
                e.printStackTrace();
            }
            // Upewnij się, że katalog istnieje
            //Files.createDirectories(Paths.get(serverDirectory));

            // Ścieżka do pliku na serwerze
            String outFilePath = serverDirectory + "/" + fileName;

            try (FileOutputStream fos = new FileOutputStream(outFilePath)) {
                workbook.write(fos);
                System.out.println("Dane zapisane pomyślnie do pliku: " + outFilePath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
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