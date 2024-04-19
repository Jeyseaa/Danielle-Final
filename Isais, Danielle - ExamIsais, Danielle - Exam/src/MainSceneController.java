import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainSceneController {

    @FXML
    private ComboBox<String> ComboStart;

    @FXML
    private ComboBox<String> ComboEnd;

    @FXML
    private ComboBox<String> ComboDiscount;

    @FXML
    private TextField fareField;

    @FXML
    private TextField TotalFare;

    @FXML
    private TextField Vat;

    @FXML
    private TextField NetCollection;

    @FXML
    private TextField Discount1;

    @FXML
    private TextField Net;

    @FXML
    private TextField TFC;

    @FXML
    private Label errorLabel;

    @FXML
    private Button calculateButton;

    private static final double BASE_FARE = 30.00;
    private static final double VAT_RATE = 0.12;

    public void initialize() {
        ComboStart.getItems().addAll("Bamban", "Capas", "Concepcion", "Tarlac City", "Gerona", "Paniqui", "Moncada");
        ComboEnd.getItems().addAll("Bamban", "Capas", "Concepcion", "Tarlac City", "Gerona", "Paniqui", "Moncada");
        ComboDiscount.getItems().addAll("None", "Senior Citizen", "Student", "PWD/Pregnant");

        BooleanBinding startFilled = Bindings.createBooleanBinding(() -> ComboStart.getValue() != null, ComboStart.valueProperty());
        BooleanBinding endFilled = Bindings.createBooleanBinding(() -> ComboEnd.getValue() != null, ComboEnd.valueProperty());
        BooleanBinding discountFilled = Bindings.createBooleanBinding(() -> ComboDiscount.getValue() != null, ComboDiscount.valueProperty());

        BooleanBinding allFilled = startFilled.and(endFilled).and(discountFilled);

        fareField.disableProperty().bind(allFilled.not());
        TotalFare.disableProperty().bind(allFilled.not());
        Vat.disableProperty().bind(allFilled.not());
        NetCollection.disableProperty().bind(allFilled.not());
        Discount1.disableProperty().bind(allFilled.not());
        Net.disableProperty().bind(allFilled.not());
        TFC.disableProperty().bind(allFilled.not());

        calculateButton.disableProperty().bind(allFilled.not());

        calculateButton.setOnAction(event -> calculateFare());
    }

    @FXML
private void calculateFare() {
    String startStation = ComboStart.getValue();
    String endStation = ComboEnd.getValue();
    String discount = ComboDiscount.getValue();

    int distance = Math.abs(ComboStart.getItems().indexOf(startStation) - ComboEnd.getItems().indexOf(endStation));
    double fare = BASE_FARE * distance;

    double discountAmount = 0.0;
    switch (discount) {
        case "Senior Citizen":
            discountAmount = fare * 0.25;
            break;
        case "Student":
            discountAmount = fare * 0.20;
            break;
        case "PWD/Pregnant":
            discountAmount = fare * 0.15;
            break;
    }

    double netFare = fare - discountAmount;
    double vat = netFare * VAT_RATE;
    double totalFareCollected = netFare + vat;

    // Calculate NetCollection
    double netCollection = totalFareCollected - vat;

    fareField.setText(String.format("%.2f", fare));
    Discount1.setText(String.format("%.2f", discountAmount));
    Net.setText(String.format("%.2f", netFare));
    Vat.setText(String.format("%.2f", vat));
    NetCollection.setText(String.format("%.2f", netCollection));
    TotalFare.setText(String.format("%.2f", fare));
    TFC.setText(String.format("%.2f", totalFareCollected));
    errorLabel.setText("");
}


    @FXML
    private void handleClose(javafx.event.ActionEvent event) {
        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
