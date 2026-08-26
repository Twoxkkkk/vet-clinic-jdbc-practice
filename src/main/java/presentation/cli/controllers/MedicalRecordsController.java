package presentation.cli.controllers;

import presentation.cli.shared.ServicesFactory;

import java.util.Scanner;

public class MedicalRecordsController extends BaseController{
    public MedicalRecordsController(Scanner scanner, ServicesFactory services) {
        super(scanner, services);
    }

    public void printOutTopDiagnosises(){
        System.out.println(
            services.medicalRecords().getTopDiagnosisReportsForTheCurrentMonth()
        );
    }
}
