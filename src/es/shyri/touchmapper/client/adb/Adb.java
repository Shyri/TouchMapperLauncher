package es.shyri.touchmapper.client.adb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by shyri on 11/09/17.
 */
public class Adb {
    //sh -c "CLASSPATH=/data/app/es.shyri.touchmapper-1/base.apk /system/bin/app_process32 /system/bin es.shyri.touchmapper.Main"

    public void connectDevice(String IP, CommandLineRunner.CommandLineCallback callback) throws IOException {
        new CommandLineRunner.Command("adb", "connect", IP + ":5555").addCallback(callback)
                                                                     .run();
    }

    public void getDevicesList(DevicesCallback callback) throws IOException {
        new CommandLineRunner.Command("adb", "devices", "-l").addCallback(result -> {
            callback.onDevices(parseDevices(result));
        })
                                                             .run();
    }

    public void pushFile(String IPDest,
                         String filePath,
                         String destPath,
                         CommandLineRunner.CommandLineCallback callback) throws IOException {
        new CommandLineRunner.Command("adb", "-s", IPDest, "push", filePath, destPath).addCallback(callback)
                                                                                      .run();
    }

    public void logcat(String IPDest, CommandLineRunner.CommandLineCallback callback) throws IOException {
        new CommandLineRunner.Command("adb", "-s", IPDest, "logcat").addCallback(callback)
                                                                    .run();
    }

    public void runMapper(String IPDest, String id, CommandLineRunner.CommandLineCallback callback) throws IOException {

        new CommandLineRunner.Command("adb", "-s", IPDest, "shell",
                                        "sh -c \"CLASSPATH=/data/app/es.shyri" + ".touchmapper-" + id + "/base.apk " +
                                        "/system/bin/app_process32 " +
                                        "/system/bin es.shyri.touchmapper.Main\"").addCallback(callback)
                                                                                  .run();
    }

    public void terminate() {
        CommandLineRunner.terminate();
    }

    private ArrayList<Device> parseDevices(String devices) {
        ArrayList<Device> deviceList = new ArrayList<>();
        try (Scanner scanner = new Scanner(devices)) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.contains(" ")) {
                    continue;
                }
                String id = line.substring(0, line.indexOf(" "));
                String name = line.substring(line.indexOf("model:") + 6, line.lastIndexOf(" "));

                Device device = new Device(id, name);
                deviceList.add(device);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deviceList;
    }

    public interface DevicesCallback {
        void onDevices(ArrayList<Device> devices);
    }
}
