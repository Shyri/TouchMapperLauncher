package es.shyri.touchmapper.client;

import com.sun.javafx.application.PlatformImpl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import es.shyri.touchmapper.client.adb.Adb;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * Created by shyri on 17/11/2017.
 */
public class MainController implements Initializable {
    @FXML
    Button loadConfigFile;
    @FXML
    Button connectAdb;
    @FXML
    Button disconnectAdb;
    @FXML
    Text fileNameText;
    @FXML
    TextField IPTextField;
    @FXML
    TextArea logTextArea;
    @FXML
    ProgressBar loadingProgressBar;

    final FileChooser fileChooser = new FileChooser();
    final public Adb adb = new Adb();

    private File selectedFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadConfigFile.setOnAction(event -> {
            selectedFile = fileChooser.showOpenDialog(loadConfigFile.getScene()
                                                                    .getWindow());
            if (selectedFile != null) {
                fileNameText.setText("File: " + selectedFile.getPath());
            } else {
                fileNameText.setText("File: none");
            }
        });
    }

    public void terminate() {
        adb.terminate();
    }

    @FXML
    public void onConnectClick() throws IOException {
        showProgress();
        adb.connectDevice(IPTextField.getText(), result -> {
            if (result != null && result.contains("connected")) {
                log(result);
                try {
                    sendFile();
                } catch (IOException e) {
                    log(e.getMessage());
                    e.printStackTrace();
                    hideProgress();
                }
            } else {
                log("Unable to connect device\n");
                hideProgress();
            }
        });
    }

    @FXML
    public void onDisconnectClick() {
        showConnectEnabled();
        terminate();
    }

    private void sendFile() throws IOException {
        if (selectedFile != null) {
            adb.pushFile(IPTextField.getText(), selectedFile.getPath(),
                         "/storage/self/primary/Android/data/es.shyri.touchmapper/files/mapping.json", result -> {
                        log(result);
                        try {
                            runMapper();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    private void runMapper() throws IOException {
        adb.getApkId(IPTextField.getText(), apkId->{
            adb.runMapper(IPTextField.getText(), apkId, result -> {
                hideProgress();
                showDisconnectEnabled();
                log(result);

                // TODO Killed case

                // TODO Unkown case
            });
        });
    }

    private void log(String log) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");

        //        if (logTextArea.getText()
        //                       .length() > 1000) {
        //            logTextArea.setText(logTextArea.getText()
        //                                           .substring(1000));
        //        }

        try {
            PlatformImpl.runAndWait(() -> logTextArea.appendText(format1.format(cal.getTime()) + " " + log));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showProgress() {
        loadConfigFile.setDisable(true);
        connectAdb.setDisable(true);
        IPTextField.setDisable(true);
        disconnectAdb.setDisable(true);
        loadingProgressBar.setVisible(true);
    }

    private void hideProgress() {
        loadConfigFile.setDisable(false);
        connectAdb.setDisable(false);
        IPTextField.setDisable(false);
        loadingProgressBar.setVisible(false);
    }

    private void showConnectEnabled() {
        loadConfigFile.setDisable(false);
        connectAdb.setDisable(false);
        disconnectAdb.setDisable(true);
        IPTextField.setDisable(false);
    }

    private void showDisconnectEnabled() {
        loadConfigFile.setDisable(true);
        connectAdb.setDisable(true);
        disconnectAdb.setDisable(false);
        IPTextField.setDisable(true);
    }
}
