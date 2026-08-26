package presentation.cli.controllers;

import presentation.cli.shared.ServicesFactory;

import java.util.Scanner;

public class BaseController {

    protected final Scanner scanner;
    protected final ServicesFactory services;

    public BaseController(Scanner scanner, ServicesFactory services){
        this.scanner = scanner;
        this.services = services;
    }
}
