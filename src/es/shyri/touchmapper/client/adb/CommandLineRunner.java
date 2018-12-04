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
        void addResult(String result) throws IOException;
    }
}
