package es.shyri.touchmapper.client.adb;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shyri on 12/09/17.
 */
public class CommandLineRunner {
    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    private static Process process;


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

    private static Thread watchProcess(final Process processs, CommandLineCallback commandLineCallback) {
        process = processs;
        return new CommandLineThread(processs, commandLineCallback);
        //        return new Thread() {
        //            public void run() {
        //                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        //                String line;
        //                try {
        //                    StringBuilder stringBuilder = new StringBuilder();
        //                    while (process.isAlive()) {
        //                        System.out.println("Process alive");
        //                        long time = System.currentTimeMillis();
        //
        //                        System.out.println("Waiting for shit");
        //
        //                        while ((line = input.readLine()) != null) {
        //                            stringBuilder.append(line);
        //                            stringBuilder.append(System.getProperty("line.separator"));
        //
        //                            if (System.currentTimeMillis() - time > 500) {
        //                                break;
        //                            }
        //                        }
        //                        System.out.println("No more shit");
        //
        //                        commandLineCallback.addResult(stringBuilder.toString());
        //                    }
        //                    System.out.println("Process killed");
        //
        //                    //                    commandLineCallback.addResult(stringBuilder.toString());
        //                } catch (IOException e) {
        //                    e.printStackTrace();
        //                    System.out.println("IO Exception");
        //                }
        //
        //                try {
        //                    System.out.println("Join process");
        //                    join();
        //                } catch (InterruptedException e) {
        //                    e.printStackTrace();
        //                    System.out.println(e.getMessage());
        //                }
        //            }
        //        };
    }

    public static void terminate() {
        if (process != null) {
            process.destroy();
        }

        if (executor != null) {
            executor.shutdownNow();
        }
    }

    public interface CommandLineCallback {
        void addResult(String result);
    }
}
