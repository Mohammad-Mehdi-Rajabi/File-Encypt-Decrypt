package Controllers.Decrypt;

import Core.Decrypt.Decrypt;
import Core.Encrypt.Encrypt;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;

public class DecryptController implements Initializable {
    @FXML
    private TextField sourceTxt;

    @FXML
    private TextField distTxt;

    @FXML
    private Button sourceChooseBtn;

    @FXML
    private Button distChooseBtn;

    @FXML
    private WebView logWebView;

    @FXML
    private Button encryptBtn;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private CheckBox showPasswordCheckBox;

    private static StringBuilder s;

    static {
        s = new StringBuilder();
        s.append("<html>");
        s.append("<head>");
        s.append("   <script language=\"javascript\" type=\"text/javascript\">");
        s.append("       function toBottom(){");
        s.append("           window.scrollTo(0, document.body.scrollHeight);");
        s.append("       }");
        s.append("   </script>");
        s.append("</head>");
        s.append("<body onload='toBottom()'>");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        distChooseBtn.setDisable(true);
        distTxt.setEditable(false);
        passwordTxt.setEditable(false);
        encryptBtn.setDisable(true);
        showPasswordCheckBox.setDisable(true);
        logWebView.getEngine().setUserStyleSheetLocation(getClass().getClassLoader().getResource("styles/HTMLStyles.css").toString());
    }

    @FXML
    void onActionDistChooseBtn(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("select Destination Directory");
        File file = directoryChooser.showDialog(null);
        if (file == null) {
            s.append("<p class=\"error\">select Distention Directory Now" + "<br>");
            s.append("<p class=\"black\">-----------------------------------------------------------------------------</p><br>");
            logWebView.getEngine().loadContent(s.toString());
        } else {
            distTxt.setText(file.getPath());
            passwordTxt.setEditable(true);
            encryptBtn.setDisable(false);
            showPasswordCheckBox.setDisable(false);
        }
    }

    @FXML
    void onActionDecryptBtn(ActionEvent event) {
        if (passwordTxt.getText().equals("")) {
            s.append("<p class=\"error\">fill password box" + "<br>");
            s.append("<p class=\"black\">-----------------------------------------------------------------------------</p><br>");
            logWebView.getEngine().loadContent(s.toString());
        } else {
            try {
                Decrypt.decrypt(sourceTxt.getText(), distTxt.getText(),passwordTxt.getText());
                s.append("<p class=\"success\">Decryption complete:" + "<br>");
                s.append("<p class=\"error\"> Note: if your file not decrypted it's because enter wrong password"+ "<br>");
                s.append("<p class=\"success\">Source File:  -> " +Decrypt.sourcePath + "<br>");
                s.append("<p class=\"success\">Distention File:  -> " + Decrypt.distPath + "<br>");
                s.append("<p class=\"black\">-----------------------------------------------------------------------------</p><br>");
                logWebView.getEngine().loadContent(s.toString());
            } catch (InvalidKeySpecException e) {
                s.append("<p class=\"error\">something wrong 1" + "<br>");
                s.append("<p class=\"black\">-----------------------------------------------------------------------------</p><br>");
                logWebView.getEngine().loadContent(s.toString());
            } catch (NoSuchPaddingException e) {
                s.append("<p class=\"error\">something wrong 2" + "<br>");
                s.append("<p class=\"black\">-----------------------------------------------------------------------------</p><br>");
                logWebView.getEngine().loadContent(s.toString());
            } catch (NoSuchAlgorithmException e) {
                s.append("<p class=\"error\">something wrong 4" + "<br>");
                s.append("<p class=\"black\">-----------------------------------------------------------------------------</p><br>");
                logWebView.getEngine().loadContent(s.toString());
            } catch (InvalidKeyException e) {
                s.append("<p class=\"error\">at least 8 char needed for password" + "<br>");
                s.append("<p class=\"black\">-----------------------------------------------------------------------------</p><br>");
                logWebView.getEngine().loadContent(s.toString());
            } catch (IOException e) {
                s.append("<p class=\"error\">something wrong 4" + "<br>");
                s.append("<p class=\"black\">-----------------------------------------------------------------------------</p><br>");
                logWebView.getEngine().loadContent(s.toString());
            }
        }
    }

    @FXML
    void onActionShowPasswordCheckBox(ActionEvent event) {
        if (showPasswordCheckBox.isSelected()) {
            String text = passwordTxt.getText();
            showPasswordCheckBox.setText(text);
        } else {
            showPasswordCheckBox.setText("");
        }
    }

    @FXML
    void onActionSourceChooseBtn(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("select source File");
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            s.append("<p class=\"error\">select Source File first" + "<br>");
            s.append("<p class=\"black\">-----------------------------------------------------------------------------</p><br>");
            logWebView.getEngine().loadContent(s.toString());
        } else {
            sourceTxt.setText(file.getPath());
            distChooseBtn.setDisable(false);
            distTxt.setEditable(true);
        }
    }


    public void onClickPasswordTxt(MouseEvent mouseEvent) {
        showPasswordCheckBox.setSelected(false);
        showPasswordCheckBox.setText("");
    }
}
