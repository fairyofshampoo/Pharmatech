package pharmatech.GUI.controllers;


import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;


public class FieldValidation {
    public static boolean isNullOrEmptyTxtField(TextField textField) {
       return textField == null || textField.getText().trim().isEmpty();
    }
    
    public static boolean isChoiceBoxSelected(ChoiceBox<String> choiceBox) {
        return choiceBox.getValue() != null && !choiceBox.getValue().isEmpty();
    }
    
    public static boolean doesNotExceedLenghtTxtField(TextField textField, int maximumLength){
        return textField.getText().length() > maximumLength; 
    }
}
