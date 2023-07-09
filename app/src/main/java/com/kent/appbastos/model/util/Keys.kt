package com.kent.appbastos.model.util

import java.text.DecimalFormat

object Keys {

    //Title
    const val TITLE = "title"
    const val DEBTS = "debts"
    const val DEBTS_ES = "deuda"
    const val INVENTORY = "inventory"
    const val CREDIT_SALE = "creditSale"
    const val CASH_SALE = "CashSale"
    const val CLIENT = "clients"

    //Data share firebase and memory and fields

    //Keys
    const val KEY = "key"
    const val KEY_DEBTS = "keyDebts"
    const val KEY_INVENTORY = "keyInventory"


    //Data in memory
    const val PROFILE = "profile"
    const val ROL = "rol"
    const val ROL_ADMIN = "admin"
    const val ROL_USER = "usuario"
    const val PROVIDER_SESSION = "provider"
    const val EMAIL = "email"

    //Data firebase
    const val INVENTORY_OF_DEBTS= "inventoryOfDebts"
    const val AMOUNT_INVENTORY = "amountInventory"
    const val NUMBER_CLIENT = "numberClient"
    const val TYPE = "type"
    const val VALUE_AMOUNT = "valueAmount"
    const val NAME_CLIENT = "nameClient"
    const val AMOUNT = "amount"
    const val DATE_TIME = "dateTime"
    const val DAY = "day"
    const val MONTH = "month"
    const val YEAR = "year"
    const val VALUE_UNIT = "valueUnit"
    const val VALUE_TOTAL = "valueTotal"
    const val AMOUNT_MIN = "amountMin"
    const val FLETE = "flete"
    const val NAME = "name"
    const val PROVIDER = "provider"
    const val VALUE_BASE = "valueBase"
    const val CATEGORY = "category"
    const val USERNAME = "username"
    const val NUMBER = "number"
    const val NUMBER_DEBTS= "numberDebts"
    const val PHONE = "phone"
    const val USER = "users"
    const val USERS_APP = "usersApp"
    const val CLIENTS = "clients"
    const val DEBTS_TOTAL = "debts"

    //Error
    const val ERROR = "error"



    //Text's visible ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //Error field's
    const val WITHOUT_DEBTS = "No nay deudas registradas"
    const val ERROR_FIELD_EMPTY = "Error: Campo Vacio"
    const val ERROR_FIELD_ZERO = "Error: No puede ser 0"
    const val ERROR_DEBTS_GREATER = "Error: Valor mayor a la deuda registrada"
    const val ERROR_WITHOUT_CLIENT = "Error: debe seleccionar un cliente"
    const val ERROR_WITHOUT_INVENTORY = "Error: no hay suficiente inventario"
    const val ERROR_GREATER_THAT_MIN = "Error: debe ser mayor que la cantidad minima"
    const val ERROR_GREATER = "Error: debe ser menor que la cantidad"

    //TOAST MSM-------------------------------------------------------------------------------------
    //MSM Toast Successfully
    const val TOAST_ADD_PAYMENT = "Se agrego el pago"
    const val TOAST_ADD_SUCCESSFULLY = "Se agrego correctamente"

    //MSM Toast Error
    const val TOAST_SOME_DATA_NOT_VALID = "Algun dato no es valido"
    const val TOAST_DATA_NOT_VALID = "Dato no valido"
    const val TOAST_NOT_FIND = "No se encontro"
    const val TOAST_NOT_ADD = "No se agrego"
    const val TOAST_ERROR_DATABASE = "Error en base de datos"
    const val TOAST_ERROR_SOME_DATA  = "Error con algun dato"
    const val TOAST_ERROR_SAVE_DATA  = "Ocurrio un error al guardar los datos"

    //MSM Toast Informative
    const val TOAST_MSM_INVENTORY = "Inventaio de repollo %s"
    const val TOAST_MSM_BUILD = "En construccion..."
    const val WELCOME = "Bienvenidos"
    const val WELCOME_AGAIN = "Bienvenidos de nuevo"

    //Alert MSM-------------------------------------------------------------------------------------
    //Title
    const val ALERT_TITLE_ERROR = "error"

    //MSM
    const val ALERT_MSM = "No se a iniciado sesion"
    const val ALERT_MSM_VALUE_MIN = "Alerta el valor minimo es de %s, ¿desea continuar?"

    //Text in buttons
    const val ALERT_BUTTON = "Aceptar"
    const val BUTTON_CONTINUE = "Continuar"
    const val BUTTON_CHANGE = "Cambiar"


    //Format
    val FORMAT_PRICE = DecimalFormat("$#,### COP")
    val FORMAT_AMOUNT = DecimalFormat("##.# und")
    const val FORMAT_DATE = "%d de %s del %d"
}