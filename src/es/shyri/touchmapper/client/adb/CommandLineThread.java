package es.shyri.touchmapper.client.adb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLineThread extends Thread {
    private Process process;
    private CommandLineRunner.CommandLineCallback commandLineCallback;

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
                long time = System.currentTimeMillis();

                while ((line = input.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(System.getProperty("line.separator"));

                    if (System.currentTimeMillis() - time > 500) {
                        break;
                    }
                }

                commandLineCallback.addResult(stringBuilder.toString());
            }
            process.getInputStream()
                   .close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
