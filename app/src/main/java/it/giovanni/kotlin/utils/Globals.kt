package it.giovanni.kotlin.utils

class Globals {

    companion object {

        var LOGCAT = "LOGCAT"
        var RUBRICA = "RUBRICA"
        var DATE_MANAGER = "DATE_MANAGER"

        var BEAN = "bean"

        // BACK INTENT KEY PARAMS
        var BACK_PARAM_KEY_LISTA_SEDI = "BACK_PARAM_KEY_LISTA_SEDI"
        var BACK_PARAM_KEY_SEARCH_ROOM = "BACK_PARAM_KEY_SEARCH_ROOM"
        var BACK_PARAM_KEY_EMPLOYEE_SEARCH = "BACK_PARAM_KEY_EMPLOYEE_SEARCH"
        var BACK_PARAM_KEY_SHOW_CAUSAL = "BACK_PARAM_KEY_SHOW_CAUSAL"

        // Request code
        var REQUEST_CODE_LISTA_SEDI = 1000
        var REQUEST_CODE_SEARCH_ROOM = 1001
        var REQUEST_CODE_EVENT_DETAIL = 1002
        var REQUEST_CODE_EVENT_WORKING_AREA = 1003
        var REQUEST_CODE_EVENT_HOME = 1004
        var REQUEST_CODE_EVENT_EMPLOYEE_SEARCH = 1005
        var REQUEST_CODE_SHOW_CAUSAL = 1006
        var REQUEST_CODE_PERMIT_DETAIL_DELETE = 1007
        var REQUEST_CODE_PERMIT_DETAIL_NEW = 1008

        // GLOBALS ACTION (EDIT/DELETE/UPDATE
        var KEY_ACTION = "ACTION_KEY"
        var ACTION_NEW = "action_new"
        var ACTION_UPDATE = "action_update"
        var ACTION_DELETE = "action_delete"

        // Internal Error
        var INTERNAL_ERROR = "INTERNAL_ERROR"

        // HARD PRIORITY ERROR
        var EXCHANGE_ERROR_BAD_GATEWAY = "EXCHANGE_ERROR_BAD_GATEWAY"
        var JWT_EXPIRED = "JWT_EXPIRED"
        var JWT_NOT_VALID = "JWT_NOT_VALID"
        var JWT_EXPIRED_KEEP_ALIVE= "JWT_EXPIRED_KEEP_ALIVE"

        // Permit - Status
        var PERMIT_STATUS_APPROVED = "APPROVED"
        var PERMIT_STATUS_APPROVING = "APPROVING"
        var PERMIT_STATUS_NOT_APPROVED = "NOT_APPROVED"
        var PERMIT_STATUS_FAILED = "FAILED"
        var PERMIT_STATUS_REMOVING = "REMOVING"
    }
}