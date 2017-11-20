package es.shyri.touchmapper.client;

import java.io.IOException;

import es.shyri.touchmapper.client.adb.Adb;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * Created by shyri on 11/09/17.
 */
public class DevicesController {
    public Adb adb = new Adb();
    @FXML
    private ListView devicesListView;
    
    @FXML
    public void onButtonClick() {
        try {
            adb.getDevicesList(devices -> {
                final ObservableList observableList = FXCollections.observableArrayList();
                observableList.setAll(devices);
                devicesListView.setItems(observableList);
                devicesListView.setCellFactory(new Callback<ListView, ListCell>() {
                    @Override
                    public ListCell call(ListView param) {
                        return new DeviceListCell();
                    }
                });
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
