package client.controllers;

import java.io.FileNotFoundException;

import client.App;
import common.controllers.Message;

/**
 * get <Code> Message </code> from server and send it to the relevant
 * controller. Usually used for transfer data from db to client.
 *@version 1.0 - 01/2020
 *@author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */
public class ClientMessages {

    /***
     * messageFromServer - The next method receives 1 Object and , divides all
     * actions into cases according to Operationtype and use the Object to perform
     * the operation and after send a msg to the client with the appropriate details
     */

    public static void messageFromServer(Message msg) {
        // System.out.println("--> ClientMessages.messageFromServer");
        Message m = msg;
        // System.out.println("m = " + m);

        switch (m.getOperationtype()) {
            case getViewPrevStage:
                ViewRequestController.Instance.getPrevStage_ServerResponse(m.getObject());
                break;
            case getTimeFromFrozen:
                requestTreatmentController.Instance.unFreezeSelectFrozenResponse(m.getObject());
                break;
            case updateUnfrozenStage:
                requestTreatmentController.Instance.unFreezeUpdateResponse(m.getObject());
                break;
            case getRequirementData:
                requestTreatmentController.Instance.setDataTable(m.getObject());
                break;
            case TenRequest:
            	homepageController.instance.setTable(m.getObject());
            	break;
            case getViewRequestData:
                ViewRequestController.Instance.setDataTable(m.getObject());
                break;
            case DECISION_DeclineUpdate:
                decisionController.instance.decisionDeclineQueryResult(m.getObject());
                break;
            case LoginResult:
                LoginController.instance.getLoginResult(m.getObject());
                break;
            case getEmployeeData:
                ManagerViewPage.Instance.setDataTable(m.getObject());
                break;
            case getSystemData:
                ManagerViewPage.Instance.setSystemData(m.getObject());
                break;
            case insertFreezedRequest:
                requestTreatmentController.Instance.freezeUpdateResponse(m.getObject());
                break;
            case InsertRequirement:
                ChangeRequestController.instance.queryResult(m.getObject());
                break;
            case InsertEvaluation:
                EvaluationReportController.instance.insertNewRequestResult(m.getObject());
                break;
            case Eval_updateRequestStatus:
                EvaluationReportController.instance.evaluationStageUpdateQueryResult(m.getObject());
                break;
            case ChangeRequest_File:
                ChangeRequestController.instance.uploadFileResult(m.getObject());
                break;
            case Allocate_GetITUsers:
                AllocateController.instance.setComboBoxesData(m.getObject());
                break;
            case Allocate_System_Incharge:
            	AllocateController.instance.setEvaluuator(m.getObject());
            	break;
            case Allocate_UpdateRoles:
                AllocateController.instance.allocQueryResult(m.getObject());
                break;
            case Allocate_SetRoles:
                AllocateController.instance.showResult(m.getObject());
                break;
            case User_getStageRoleObject:
                App.user.setStageRoleServerResponse(m.getObject());
                break;
            case User_getStudentAccess:
                App.user.setStudentPermission(m.getObject());
                break;
            case User_getOrgRole:
                App.user.setOrgRoleServerResponse(m.getObject());
                break;

            case SUPERVISOR_REMARKS:
                requestTreatmentController.Instance.queryResult(m.getObject());
                break;
            case updateRequestStatus:
                requestTreatmentController.Instance.freezeServerResponse(m.getObject());
                break;
            case EVAL_GetAllReportsByRID:
                EvaluationReportController.instance.setFieldsData_ServerResponse(m.getObject());
                break;
            case DECISION_GetAllReportsByRID:
                decisionController.instance.setFieldsData_ServerResponse(m.getObject());
                break;
            case DECI_UpdateDB:
                decisionController.instance.queryResult(m.getObject());
                break;
            case VALID_updateRequestStatus:
                ValidationController.instance.setValidationTable_ServerResponse(m.getObject());
                break;

            case Clousre_UpdateRequestStatus:
                ClosureController.instance.closureQueryResult(m.getObject());
                break;
            case VIEWRequest_confirmRequest:
                ViewRequestController.Instance.queryResult(m.getObject());
                break;
            case VAL_GetAllReportsByRID:
                ValidationController.instance.queryResult(m.getObject());
                break;

            case EXE_UpdateDB:
                ExecutionController.instance.queryResult(m.getObject());
                break;

            case PreEVAL_SetInitStat:
            case PreEVAL_SetConfirmationStatus:
                PreEvaluationController.instance.updateStatus_serverResponse(m.getObject());
                break;
            case PreEVAL_getData:
                PreEvaluationController.instance.getCurrentReqestedDays_ServerResponse(m.getObject());
                break;
            case EXE_GetInitData:
                ExecutionController.instance.checkPreConditions_ServerResponse(m.getObject());
                break;
            case Closure_Init:
                ClosureController.instance.checkPreConditions_ServerResponse(m.getObject());
                break;
            case VAL_GetInitData:
                ValidationController.instance.checkPreConditions_ServerResponse(m.getObject());
                break;
            // pre EXE
            case PreEXE_SetInitStat:
            case PreEXE_SetConfirmationStatus:
                PreExecutionController.instance.updateStatus_serverResponse(m.getObject());
                break;
            case PreEXE_getData:
                PreExecutionController.instance.getCurrentReqestedDays_ServerResponse(m.getObject());
                break;
            case VALID_UpdateDB:
                ValidationController.instance.queryResult(m.getObject());
                break;
            case EVAL_UpdateDB:
                EvaluationReportController.instance.queryResult(m.getObject());
                break;
            case ChangeRequest_DownloadFile:
                requestTreatmentController.Instance.DownloadFiles_ServerResponse(m.getObject());
                break;
            // pre Eval


            case Extension_submit:
                ExtensionController.instance.InsertOrUpdate_ServerResponse(m.getObject());
                break;
            case ClousreEmail:
                ClosureController.instance.emailResponse(m.getObject());
                break;
            case mailToDirectorExtension:
            	ExtensionController.instance.emailResponse(m.getObject());
            	break;
            case mailToDirectorRequestChange:
            	requestTreatmentController.Instance.emailResponse(m.getObject());
            	break;
            case ChangeRequest_getStageObject:
                requestTreatmentController.Instance.appendStageObject_ServerResponse(m.getObject());
                break;
            case updateSystems:
                ManagerViewPage.Instance.getquery(m.getObject());
                break;
            case PreValidation_GetCOMMITEE_MEMBERS:
                PreValidationController.instance.setComboBoxesData(m.getObject());
                break;
            case PreValidation_SetRole:
                PreValidationController.instance.queryResult(m.getObject());
                break;
            case DECISION_GetPrevStage:
                decisionController.instance.appendPrevStageObject_ServerResponse(m.getObject());
                break;
            case DECISION_updateRequestStatus:
                decisionController.instance.queryResult2(m.getObject());
                break;
            case VALID_UpdateRepeated:
                ValidationController.instance.queryResult2(m.getObject());
                break;
            case VALID_GetPrevStage:
                ValidationController.instance.appendPrevStageObject_ServerResponse(m.getObject());
                break;
            case VALID_GetReport:
                ValidationController.instance.getReport_ServerResponse(m.getObject());
                break;
            case EXECUTION_GetFailReport:
                ExecutionController.instance.getReport_ServerResponse(m.getObject());
                break;
            case InsertReport:


                ReportGenerateController.instance.openNewReport(m.getObject());
                break;
            case OpenReport:
                try {
                    ViewReportsController.instance.createInPC(m.getObject());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case GetReports:
                ViewReportsController.instance.setReportsToList(m.getObject());
                break;
            case ForceUpdateUsersPermissions:
                App.user.updatePermissions();
                break;
            case Manager_updateRoleInOrg:
                ManagerViewPage.Instance.appointment_ServerResponse(m.getObject());
                break;
            case Main_getMyTotalRequests:
                homepageController.instance.Main_getMyTotalRequests_Response(m.getObject());
                break;
            case Main_getMyRequestTreatment:
                homepageController.instance.Main_getMyActiveRequests_Response(m.getObject());
                break;
            case Main_getMyActiveRequests:
                homepageController.instance.Main_getMyRequestTreatment_Response(m.getObject());
                break;
            default:
                break;

        }
    }
}// class