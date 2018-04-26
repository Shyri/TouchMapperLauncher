package es.shyri.touchmapper.client.adb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLineThread extends Thread {
    private Process process;
    private CommandLineRunner.CommandLineCallback commandLineCallback;

    private boolean scanningLines = false;

    CommandLineThread(Process process, CommandLineRunner.CommandLineCallback commandLineCallback) {
        this.process = process;
        this.commandLineCallback = commandLineCallback;
    }

    @Override
    public void run() {
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            while (process.isAlive()) {
                System.out.println("Process alive ");
                long time = System.currentTimeMillis();

                System.out.println("Waiting for shit");

                while ((line = input.readLine()) != null) {
                    System.out.println("Line: " + line);

                    stringBuilder.append(line);
                    stringBuilder.append(System.getProperty("line.separator"));

                    if (System.currentTimeMillis() - time > 500) {
                        break;
                    }
                }

                System.out.println("No more shit");

                commandLineCallback.addResult(stringBuilder.toString());
            }
            System.out.println("Process killed");
            process.getInputStream()
                   .close();
            //                    commandLineCallback.addResult(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Exception");
        }

        try {
            System.out.println("Join process");
            join();
        } catch (InterruptedException e) {
            System.out.println("Interrupt Exception");
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void interrupt() {
        System.out.println("Interrupt requested");
        try {
            process.getInputStream()
                   .close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        process.destroy();
        super.interrupt();
    }
}
