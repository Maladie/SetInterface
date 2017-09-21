package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Controller {

    @FXML
    private Button addButton;
    @FXML
    private TextField valueTextField;
    @FXML
    private Label beginLabel;
    @FXML
    private Label endLabel;
    @FXML
    private HBox labelBox;
    @FXML
    private Button removeButton;
    @FXML
    private Button removeSetButton;
    @FXML
    private Button addSetButton;
    private Set mainSet;

    public void initialize() {
        mainSet = new Set();
        addButton.setDefaultButton(true);
        setMainLabelsRed();
        addButtonOnAction();
        removeButtonOnAction();
        addSetButtonOnAction();
        removeSetButtonOnAction();
        mainLabelsOnAction();
    }

    private void mainLabelsOnAction() {
        mainLabelOnAction(beginLabel);
        mainLabelOnAction(endLabel);
    }

    private void mainLabelOnAction(Label mainLabel){
        mainLabel.setOnMouseClicked(event1 -> {
            labelBox.getChildren().filtered(node -> node instanceof Label).stream().map(node -> (Label) node).forEach(label -> label.setTextFill(Paint.valueOf("Black")));
            setMainLabelsRed();
        });
    }

    private void setMainLabelsRed(){
        beginLabel.setTextFill(Paint.valueOf("Red"));
        endLabel.setTextFill(Paint.valueOf("Red"));
    }

    private void addButtonOnAction() {
        addButton.setOnAction(event -> {
            String textFieldValue = valueTextField.getText();
            addNewValue(textFieldValue);
            resetValueTextField();
            System.out.println(mainSet);
        });
    }

    private void removeButtonOnAction() {
        removeButton.setOnAction(event -> {
            removeLabel();
            System.out.println(mainSet);
        });
    }

    private void addSetButtonOnAction() {
        addSetButton.setOnAction(event -> {
            Optional<Label> optionalLabel = getRedSetList().stream().filter(label -> label.getText().equals("{")).findFirst();
            if (optionalLabel.isPresent()) {
                int index = labelBox.getChildren().indexOf(optionalLabel.get()) + 1;
                Label newLabel = new Label();
                newLabel.setText("},");
                newLabel.setFont(Font.font(20));
                labelBox.getChildren().add(index, newLabel);
                SetLabel newLabel2 = new SetLabel();
                if (optionalLabel.get().equals(beginLabel)) {
                    mainSet.add(newLabel2.getSet());
                } else {
                    SetLabel parentLabel = (SetLabel) optionalLabel.get();
                    parentLabel.add(newLabel2.getSet());
                }
                newLabel2.setText("{");
                newLabel2.setFont(Font.font(20));
                labelBox.getChildren().add(index, newLabel2);
                newLabel.setOnMouseClicked(event1 -> {
                    labelBox.getChildren().filtered(node -> node instanceof Label).stream().map(node -> (Label) node).forEach(label -> label.setTextFill(Paint.valueOf("Black")));
                    newLabel.setTextFill(Paint.valueOf("Red"));
                    newLabel2.setTextFill(Paint.valueOf("Red"));

                });

                newLabel2.setOnMouseClicked(event1 -> {
                    labelBox.getChildren().filtered(node -> node instanceof Label).stream().map(node -> (Label) node).forEach(label -> label.setTextFill(Paint.valueOf("Black")));
                    newLabel2.setTextFill(Paint.valueOf("Red"));
                    newLabel.setTextFill(Paint.valueOf("Red"));
                });
            }
            ;
        });
    }

    private void removeSetButtonOnAction() {
        removeSetButton.setOnAction(event -> {
            removeSet();
            System.out.println(mainSet);
        });
    }

    private void addNewValue(String textFieldValue) {
        Optional<Label> optionalLabel = getRedSetList().stream().filter(label -> label.getText().equals("{")).findFirst();
        if (optionalLabel.isPresent()) {
            int index = labelBox.getChildren().indexOf(optionalLabel.get()) + 1;
            if (optionalLabel.get().equals(beginLabel)) {
                if (!mainSet.contains(textFieldValue)) {
                    mainSet.add(textFieldValue);
                    addNewLabel(textFieldValue, index);
                }
            } else {
                SetLabel parentLabel = (SetLabel) optionalLabel.get();
                if (!parentLabel.getSet().contains(textFieldValue)) {
                    parentLabel.add(textFieldValue);
                    addNewLabel(textFieldValue, index);
                }
            }
        }
    }

    private void addNewLabel(String textFieldValue, int index) {
        Label newLabel = new Label();
        newLabel.setText(textFieldValue + ",");
        newLabel.setFont(Font.font(20));
        labelBox.getChildren().add(index, newLabel);
        newLabel.setOnMouseClicked(event -> {
            labelBox.getChildren().filtered(node -> node instanceof Label).stream().map(node -> (Label) node).forEach(label -> label.setTextFill(Paint.valueOf("Black")));
            newLabel.setTextFill(Paint.valueOf("Red"));
        });
    }

    private void removeLabel() {
        Optional<Label> optionalLabel = labelBox.getChildren().filtered(node -> node instanceof Label).stream().map(node -> (Label) node)
                .filter(label -> label.getTextFill().equals(Color.RED) && !(label.getText().equals("{") || label.getText().equals("},"))).findFirst();
        if (optionalLabel.isPresent()) {
            String valueToRemoveWithComa = optionalLabel.get().getText();
            String valueToRemoveWithoutComa = valueToRemoveWithComa.substring(0, valueToRemoveWithComa.length()-1);
            removeObjectFromSet(optionalLabel, valueToRemoveWithoutComa);
            labelBox.getChildren().remove(optionalLabel.get());
        }
    }

    private List<Label> getRedSetList() {
        return labelBox.getChildren().filtered(node -> node instanceof Label).stream().map(node -> (Label) node)
                .filter(label -> label.getTextFill().equals(Color.RED) && (label.getText().equals("{") || label.getText().equals("},"))).collect(Collectors.toList());
    }

    private void removeSet() {
        Optional<Label> optionalStartLabel = getRedSetList().stream().filter(label -> label.getText().equals("{")).findFirst();
        Optional<Label> optionalEndLabel = getRedSetList().stream().filter(label -> label.getText().equals("},")).findFirst();
        if (optionalStartLabel.isPresent()) {
            int startIndex = getIndexOfNodeFromLabelBox(optionalStartLabel);
            int endIndex = getIndexOfNodeFromLabelBox(optionalEndLabel);
            removeObjectFromSet(optionalStartLabel, ((SetLabel) optionalStartLabel.get()).getSet());

            for (int i = startIndex; i <= endIndex; i++) {
                labelBox.getChildren().remove(startIndex);
            }
        }
    }

    private void removeObjectFromSet(Optional<Label> optionalLabel, Object objectToRemove){
        Label parentLabel = getParentSetLabel(optionalLabel);
        if(parentLabel.equals(beginLabel)) {
            mainSet.remove(objectToRemove);
        }else {
            SetLabel parentSetLabel = (SetLabel) parentLabel;
            parentSetLabel.remove(objectToRemove);
        }
    }

    private int getIndexOfNodeFromLabelBox(Optional<Label> optionalLabel) {
        return labelBox.getChildren().indexOf(optionalLabel.get());
    }

    private Label getParentSetLabel(Optional<Label> optionalLabel){
        int valueIndex = getIndexOfNodeFromLabelBox(optionalLabel);
        Label parentLabel = null;
        for(int i = valueIndex-1; i >= 0; i--){
            Label temporaryLabel = (Label) labelBox.getChildren().get(i);
            if(temporaryLabel.getText().equals("{")){
                parentLabel = temporaryLabel;
                break;
            }
        }
        return parentLabel;
    }

    private void resetValueTextField() {
        valueTextField.clear();
    }
}
