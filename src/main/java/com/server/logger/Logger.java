package com.server.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private SimpleDateFormat simpleDateFormat;


    public Logger() {
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    }

    public void printConsoleLog(String message){
        System.out.println(simpleDateFormat.format(new Date()) + " " + message);
    }

    public void printFileLog(String message) throws IOException {
        File file = new File("log.txt");
        if (!file.exists()) file.createNewFile();
        FileWriter writer = new FileWriter("log.txt", true);
        writer.write(message);
        writer.flush();
        writer.close();
    }
}
