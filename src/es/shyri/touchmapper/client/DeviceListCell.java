package es.shyri.touchmapper.client;

import es.shyri.touchmapper.client.adb.Device;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * Created by shyri on 13/09/17.
 */
public class DeviceListCell extends ListCell<Device> {
    @FXML
    VBox vBox;
    @FXML
    Text deviceNameTextView;
    @FXML
    Button connectDeviceButton;

    public DeviceListCell() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("list_item_device.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateItem(Device item, boolean empty) {
        if (empty) {
            setGraphic(null);
        } else {
            deviceNameTextView.setText(item.getName());
            setGraphic(vBox);
        }
    }
}
