package org.rmj.engr.reports.classes;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.engr.parameter.agent.XMProject;

public class InventoryCriteriaController implements Initializable {
    @FXML
    private AnchorPane dataPane;
    @FXML
    private StackPane stack;
    @FXML
    private Pane pnePresentation;
    @FXML
    private RadioButton radioBtn01;
    @FXML
    private RadioButton radioBtn02;
    @FXML
    private Pane pneGroup;
    @FXML
    private RadioButton radioBtn03;
    @FXML
    private Button btnOk;
    @FXML
    private Button btnCancel;
    @FXML
    private RadioButton radioBtn04;
    @FXML
    private RadioButton radioBtn05;
    @FXML
    private Button btnExit;
    @FXML
    private FontAwesomeIconView glyphExit;
    @FXML
    private Pane pnePresentation1;
    @FXML
    private TextField txtField01;
    
    private boolean pbCancelled = true;
    private String psPresentation = "";
    private String psGroupBy = "";
    private String psProjCode = "";
    
    private boolean pbDetailedOnly;
    
    ToggleGroup tgPresentation;
    ToggleGroup tgGroupBy;
    
    public boolean isCancelled(){return pbCancelled;}
    public String Presentation(){return psPresentation;}
    public String GroupBy(){return psGroupBy;}
    public String ProjectCode() {return psProjCode;}
    
    public void setGRider(GRider foValue){poGRider = foValue;}
    
    public void isDetailedOnly(boolean fbValue){pbDetailedOnly = fbValue;}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnExit.setOnAction(this::cmdButton_Click);
        btnOk.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        
        radioBtn01.setOnAction(this::radioButton_Click);
        radioBtn02.setOnAction(this::radioButton_Click);
        radioBtn03.setOnAction(this::radioButton_Click);
        radioBtn04.setOnAction(this::radioButton_Click);
        radioBtn05.setOnAction(this::radioButton_Click);
        
        tgPresentation = new ToggleGroup();
        tgGroupBy = new ToggleGroup();
        
        tgPresentation.getToggles().addAll(radioBtn01, radioBtn02);
        tgGroupBy.getToggles().addAll(radioBtn03, radioBtn04, radioBtn05);
        
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        
        initButton();
        
        txtField01.requestFocus();
        pbLoaded = true;
        
    }
    
    private Stage getStage(){
        return (Stage) btnOk.getScene().getWindow();
    }
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();        
        String lsValue = txtField.getText().trim();
        
        switch (event.getCode()){
            case F3:
                XMProject instance = new XMProject(poGRider, poGRider.getBranchCode(), true);
                if (instance.browseRecord(psProjCode, false)){
                    psProjCode = (String) instance.getMaster("sProjCode");
                    txtField01.setText((String) instance.getMaster("sProjDesc"));
                } else {
                    psProjCode = "";
                    txtField01.setText("");
                }
        }
    }
    
    private void initButton(){
        radioBtn01.setDisable(pbDetailedOnly);
        
        radioBtn02.setSelected(true);
        radioBtn03.setSelected(true);
        psPresentation = "1";
        psGroupBy = "";
        
        psProjCode = poGRider.getBranchCode();
        
        XMProject instance = new XMProject(poGRider, poGRider.getBranchCode(), true);
        if (instance.browseRecord(psProjCode, true))
            txtField01.setText((String) instance.getMaster("sProjDesc"));
        else 
            txtField01.setText("");
    }
    
    private void radioButton_Click(ActionEvent event){
        String lsRadio = ((RadioButton) event.getSource()).getId();
        switch (lsRadio){
            case "radioBtn01":
                pneGroup.setDisable(true);
                radioBtn03.setSelected(true);
                psPresentation = "0";
                psGroupBy = "";
                break;
            case "radioBtn02":
                pneGroup.setDisable(false);
                radioBtn03.setSelected(true);
                psPresentation = "1";
                break;
            case "radioBtn03":
                psGroupBy = ""; break;
            case "radioBtn04":
                psGroupBy = "sBinNamex"; break;
            case "radioBtn05":
                psGroupBy = "sInvTypCd";
        }
        
    }
    
    private void cmdButton_Click (ActionEvent event){
        String lsButton = ((Button)event.getSource()).getId();
        switch(lsButton){
            case "btnCancel":
                pbCancelled = true; break;
            case "btnOk":
                pbCancelled = false; break;
            case "btnExit":
                pbCancelled = true; break;
            default:
                ShowMessageFX.Warning(null, InventoryCriteriaController.class.getSimpleName(), "Button with name "+ lsButton +" not registered!");
        }
        CommonUtils.closeStage(btnExit);
    }
        
    private static GRider poGRider;
    private boolean pbLoaded = false;
    
}
