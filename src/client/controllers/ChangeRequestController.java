package client.controllers;

import client.App;
import common.controllers.Message;
import common.controllers.OperationType;
import common.entity.ChangeRequest;
import common.entity.MyFile;
import common.entity.StageName;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 
 *@version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */

/**
 * 
 * <h2>Change Request Window</h2>
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */
public class ChangeRequestController extends AppController implements Initializable {
    public static ChangeRequestController instance;
    protected ChangeRequest thisRequest;
    @FXML
    private ComboBox<String> infoSystemCombo;

    @FXML
    private TextArea ExistingConditionText;

    @FXML
    private TextArea suggestedText;

    @FXML
    private TextArea reasonText;

    @FXML
    private TextArea remarksText;

    @FXML
    private Button submitRequestBtn;

    @FXML
    private Button browsebtn;

    @FXML
    private Text ReasonEmptyWarning;

    @FXML
    private Text suggestedConditionEmptyWarning;

    @FXML
    private Text ExistingconditionEmptyWarning;

    @FXML
    private Text infoSystemEmptyWarning;

    @FXML
    private TextArea fileNames;

    private Stage stage;
    private List<File> filelist;
    static int rid;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        resetWarnings();
        fileNames.setVisible(false);
        infoSystemCombo.getItems().addAll("Moodle", "Information Station", "Library", "Class Computers", "Labs",
                "Computer Farm", "Collage website");
    }


    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>submit request if all conditions are met</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param event submit button pressed
     */
    @FXML
    void submitBtnAction(ActionEvent event) {
        resetWarnings();
        if (!formChecker())
            return;
        insertDataToDB();

    }

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Clear all fields</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    void clearAll() {
        ExistingConditionText.clear();
        suggestedText.clear();
        reasonText.clear();
        remarksText.clear();
        fileNames.clear();
        fileNames.setVisible(false);
        infoSystemCombo.getSelectionModel().clearSelection();

    }


    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Convert file list to a string that with the files names</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param filelist (List<File>) list of files attached
     *@return (String) list of files attached 
     */
    private String printNameFiles(List<File> filelist) {
        StringBuilder str = new StringBuilder("");
        if (filelist != null) {
            for (File i : filelist) {
                str.append(i.getName());
                str.append("\n");
            }
        }
        return str.toString();
    }


    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Send query to create new change request in DB</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    private void insertDataToDB() {
        Calendar currenttime = Calendar.getInstance(); // creates the Calendar object of the current time
        Date current = new Date((currenttime.getTime()).getTime()); // creates the sql Date of the above created object
        // Date dueDate = Date.valueOf(thisRequest.getDueDate().toLocalDate());
        try {
            String pathServerFiles = printNameFiles(filelist);
            System.out.println(pathServerFiles);
            String query = "INSERT INTO `Requests`(`USERNAME`, `Position`, `Email`, `Existing_Cond`, `Wanted_Change`, `Treatment_Phase`, `Status`, `Reason`, `SystemID`, `Comments`, `Date`,`FILE`)"
                    + "VALUES" + "('" + App.user.getUserName() + "','" + App.user.getPosition() + "','"
                    + App.user.getEmail() + "','" + ExistingConditionText.getText() + "','" + suggestedText.getText()
                    + "','" + StageName.INIT.toString() + "','WAITING(SUPERVISOR)','"
                    + reasonText.getText() + "','" + infoSystemCombo.getValue() + "','" + remarksText.getText() + "','"
                    + current + "','" + "-" + "');";

            OperationType ot = OperationType.InsertRequirement;

            App.client.handleMessageFromClientUI(new Message(ot, query));

        } catch (Exception e) {
            showAlert(AlertType.WARNING, "Insert data Failed", "try again", null);
        }
    }

    /**
     *
     *
     * @param event
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Open file browser </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param event  press on browse button we open a file browser window.
     */
    @FXML
    void browsebtnAction(ActionEvent event) {
        openFile();
    }
/**
 * get the string of the path of the file and create zip from this path
 * @param filename
 */
    void createZip(String filename) {
        if (filelist == null) {
            return;
        }
        File myZip = new File(filename);
        File[] files = new File[filelist.size()];
        files = filelist.toArray(files);
        if (!filelist.isEmpty()) {
            try {
                zipFile(files, myZip);
                UploadRequestFilesToServer(myZip);
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Error!", "Files Compressions Error.", null);
            }
        }

    }

    
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Create a file browser</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        filelist = fileChooser.showOpenMultipleDialog(stage);
        if (filelist == null) {
            return;
        }
        fileChooser.setTitle("Open Resource File");
        fileNames.setVisible(true);
        fileNames.setText(printNameFiles(filelist));
    }

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Upload zip file to the server </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param file (File) file to upload
     *@return result(boolean) if upload was successful
     */
    private boolean UploadRequestFilesToServer(File file) {
        Message msgToServer;
        MyFile myFile = new MyFile(file.getName(), file.getPath());
        try {
            byte[] mybytearray = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            myFile.initArray(mybytearray.length);
            myFile.setSize(mybytearray.length);
            bis.read(myFile.getMybytearray(), 0, mybytearray.length);
            msgToServer = new Message(OperationType.ChangeRequest_File, myFile);
            App.client.handleMessageFromClientUI(msgToServer);
            fis.close();
            bis.close();
            file.delete();

            return true;
        } catch (Exception e) {
            System.out.println("Error file in client: ");
            e.printStackTrace();
            return false;
        }
    }

    
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Reset warnings fields.</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    private void resetWarnings() {
        ExistingconditionEmptyWarning.setVisible(false);
        infoSystemEmptyWarning.setVisible(false);
        ReasonEmptyWarning.setVisible(false);
        suggestedConditionEmptyWarning.setVisible(false);
    }

    /**
     * check missing fields
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Check form, show warnings</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@return (boolean) true: all fields were filled, false: at least one wasn't filled
     */
    private Boolean formChecker() {
        boolean flag = true;
        String existingCondition=ExistingConditionText.getText();

        if (ExistingConditionText.getText().isEmpty()) {
            ExistingconditionEmptyWarning.setVisible(true);
            flag = false;
        }
        if (reasonText.getText().isEmpty()) {
            ReasonEmptyWarning.setVisible(true);
            flag = false;
        }
        if (suggestedText.getText().isEmpty()) {
            suggestedConditionEmptyWarning.setVisible(true);
            flag = false;
        }
        if (infoSystemCombo.getSelectionModel().isEmpty()) {
            infoSystemEmptyWarning.setVisible(true);
            flag = false;
        }
        return flag;
    }


    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Response from server, check if request was add to DB.  </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object (Object)  request id
     */
    public void queryResult(Object object) {
        rid = (int) object;
        if (rid > 0) {
            if (filelist != null) {
                createZip("Request_" + rid + ".zip");
            } else {
                showAlert(AlertType.INFORMATION, "Request #" + rid ,"Your Request had been submitted successfully" + "\nThank you! ",
                        null);
                clearAll();
                //loadPage("Homepage");
            }
        } else
            showAlert(AlertType.ERROR, "Error!", "Could not compress the files.", null);

    }

    /**
     * Check the return from the server of the file.
     * *ChangeRequest_File Operation
     * @param object the object that return from the server is boolean
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p></p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object
     */
    public void uploadFileResult(Object object) {
        boolean fileRes = (boolean) object;
        if (fileRes) {
            showAlert(AlertType.INFORMATION, "Request #" + rid ,"Your Request had been submitted successfully" + "\nThank you! ",
                    null);
            clearAll();
            //loadPage("Homepage");
        } else
            showAlert(AlertType.ERROR, "Error!", "File upload Error.", null);
    }

    /**
     *
     *
     * @param object the object that return from the server is boolean
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p> Server Response,Check the the file upload result.</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object (boolean) result of request and file insert into DB
     */
    public void uploadFileAndqueryResult(Object object) {
        boolean fileRes = (boolean) object;
        if (fileRes) {
            showAlert(AlertType.INFORMATION, "Request was sent successfuly",
                    "We will mail you a receipt to " + App.user.getEmail() + "\r\n" + "\t\t\t\t\tThank you! ", null);
            //loadPage("Homepage");
        } else
            showAlert(AlertType.ERROR, "Error!", "File upload Error.", null);
    }


    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>create a Zip file from all attached files</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param files (File[]) files to add to zip
     *@param targetZipFile (File) zip file to add to.
     *@throws IOException can be thrown when copying...
     */
    public static void zipFile(final File[] files, final File targetZipFile) throws IOException {
        try {
            FileOutputStream fos = new FileOutputStream(targetZipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            byte[] buffer = new byte[256];
            for (int i = 0; i < files.length; i++) {
                File currentFile = files[i];
                if (!currentFile.isDirectory()) {
                    ZipEntry entry = new ZipEntry(currentFile.getName());
                    FileInputStream fis = new FileInputStream(currentFile);
                    zos.putNextEntry(entry);
                    int read = 0;
                    while ((read = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, read);
                    }
                    zos.closeEntry();
                    fis.close();
                }
            }
            zos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

    }
}