package es.shyri.touchmapper.client.adb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shyri on 12/09/17.
 */
public class CommandLineExecutor {
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static class Command {
        private String[] command;
        private CommandLineCallback callback;

        public Command(String... command) {
            this.command = command;
        }

        public Command addCallback(CommandLineCallback callback) {
            this.callback = callback;
            return this;
        }

        public void run() throws IOException {
            runCommand(callback, command);
        }
    }

    private static void runCommand(CommandLineCallback callback, String... params) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(params);
        builder.redirectErrorStream(true);
        final Process process = builder.start();
        executor.submit(watchProcess(process, callback));
    }

    private static Thread watchProcess(final Process process, CommandLineCallback commandLineCallback) {
        return new Thread() {
            public void run() {
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = input.readLine()) != null) {
                        stringBuilder.append(line);
                        stringBuilder.append(System.getProperty("line.separator"));
                    }

                    commandLineCallback.addResult(stringBuilder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    interface CommandLineCallback {
        void addResult(String result);
    }
}
