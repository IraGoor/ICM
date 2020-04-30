package common.controllers;

public enum OperationType {
    Logout,
    InsertRequirement,
    getRequirementData,
    LoginAsStudent,
    LoginAsEmployee,
    InsertEvaluation,
    getViewRequestData,
    // Results:

    LoginResult, ChangeRequest_File,
    Allocate_GetITUsers,
    Allocate_SetRoles,
    User_SetStageRole,
    User_getStageRoleObject,
    User_getOrgRole,
    updateRequestStatus,
    EVAL_GetAllReportsByRID,
    DECISION_GetAllReportsByRID,
    VAL_GetAllReportsByRID,
    EVAL_GetInitData,
    EXE_GetInitData,
    PreEVAL_SetInitStat,
    PreEVAL_SetConfirmationStatus,
    PreEVAL_getData,
    PreEXE_SetConfirmationStatus,
    PreEXE_SetInitStat,
    PreEXE_getData,
    EXE_UpdateDB,
    VALID_UpdateDB,
    EVAL_UpdateDB,
    DECI_UpdateDB,
    ChangeRequest_DownloadFile,
    SUPERVISOR_REMARKS,
    Extension_submit,
    ChangeRequest_getStageObject, getEmployeeData, updateRoleInOrg, updateSystems, deleteMember, PreValidation_SetRole, PreValidation_GetCOMMITEE_MEMBERS, getSystemData, VAL_GetInitData,
    DECISION_SendEmailToUser, Allocate_UpdateRoles,
    DECISION_GetPrevStage, DECISION_updateRequestStatus, VALID_updateRequestStatus, EXECUTION_GetFailReport, VALID_GetReport, VALID_GetPrevStage,
    VALID_UpdateRepeated,

    //Report
    GenreateReport, InsertReport, DECISION_DeclineUpdate, Allocate_UpdateRequestStatus, Clousre_UpdateRequestStatus, Eval_updateRequestStatus, OpenReport, GetReports,

    ForceUpdateUsersPermissions, Manager_updateRoleInOrg, VIEWRequest_confirmRequest, Closure_Init,User_getStudentAccess,

    Main_getMyTotalRequests, Main_getMyActiveRequests, Main_getMyRequestTreatment, getTimeFromFrozen, updateUnfrozenStage, insertFreezedRequest, getViewPrevStage, ClousreEmail, TenRequest, mailToDirectorExtension, mailToDirectorRequestChange, Allocate_System_Incharge,
    VALID_UpdatePrevStage;


}
