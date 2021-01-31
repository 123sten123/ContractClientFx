package sample.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import sample.pojo.Contract;

import javafx.scene.control.TableColumn;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;

public class Controller {

    private ObservableList<Contract> usersData = FXCollections.observableArrayList();

    @FXML
    private TableView<Contract> tableUsers;

    @FXML
    private TableColumn<Contract, Integer> idColumn;

    @FXML
    private TableColumn<Contract, String> dateColumn;

    @FXML
    private TableColumn<Contract, String> numberContract;

    @FXML
    private TableColumn<Contract, String> chekDate;

    @FXML
    private void initialize() throws Exception {

        doLoad();
        idColumn.setCellValueFactory(new PropertyValueFactory<Contract, Integer>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Contract, String>("dateContract"));
        numberContract.setCellValueFactory(new PropertyValueFactory<Contract, String>("number"));
        chekDate.setCellValueFactory(new PropertyValueFactory<Contract, String>("dataChek"));

        TableColumn select = new TableColumn("Чек");
        select.setMinWidth(200);
        select.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Contract, CheckBox>, ObservableValue<CheckBox>>() {
            @Override
            public ObservableValue<CheckBox> call(
                    TableColumn.CellDataFeatures<Contract, CheckBox> arg0) {
                Contract contract = arg0.getValue();

                CheckBox checkBox = new CheckBox();

                checkBox.selectedProperty().setValue(contract.isChek());



                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov,
                                        Boolean old_val, Boolean new_val) {

                        contract.setChek(new_val);

                    }
                });

                checkBox.setDisable(true);

                return new SimpleObjectProperty<CheckBox>(checkBox);

            }
        });
        tableUsers.getColumns().addAll(select);




        tableUsers.setItems(usersData);
    }


    private ArrayList<Contract> doLoad() throws Exception {
        HttpGet get = new HttpGet("http://localhost:8082/api/users");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpClient.execute(get)) {
            HttpEntity entity = response.getEntity();
            String json = EntityUtils.toString(entity);
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            Type contactListType = new TypeToken<ArrayList<Contract>>() {
            }.getType();
            ArrayList<Contract> contracts = gson.fromJson(json, contactListType);

            for (Contract contract: contracts) {
                if(DAYS.between(contract.getDataChek().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()) > 60){
                    contract.setChek(false);
                }
                else{
                    contract.setChek(true);
                }
            }
            usersData.addAll(contracts);

            return contracts;
        }catch (Exception e){
            return null;
        }
    }

}