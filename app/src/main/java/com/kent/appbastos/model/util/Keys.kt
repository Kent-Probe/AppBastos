package com.kent.appbastos.model.util

import com.google.i18n.phonenumbers.PhoneNumberUtil
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
    const val USERS_APP = "usersApp"
    const val USER = "users"

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
    const val HOUR = "hour"
    const val MINUTE = "minute"
    const val SECOND = "second"
    const val MILLISECOND = "milliSecond"

    const val VALUE_UNIT = "valueUnit"
    const val REFERENCE = "reference"
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
    const val CLIENTS = "clients"
    const val DEBTS_TOTAL = "debts"
    const val FIRST_NAME = "firstName"
    const val LAST_NAME = "lastName"

    //Error
    const val ERROR = "error"
    const val TEMP = "temporal"

    //value of response ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    const val MSM_LOADING = "cargando..."
    const val MSM_RESPONSE_NEGATIVE = "sin coincidencias"

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

    //TOAST MSM------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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
    const val FORMAT_AMOUNT_FRACTION = "%s und"
    const val FORMAT_DATE = "%d de %s del %d, %d:%d"
    const val FORMAT_STRING = "%s %s"
    const val FORMAT_TITLE_RECEIPT = "Detalles de la venta a %s"

    //Format receipt
    const val FORMAT_RECEIPT_DATE_TIME = "Fecha: %s"
    const val FORMAT_RECEIPT_CLIENT = "Nombre del cliente: %s"
    const val FORMAT_RECEIPT_TYPE = "Tipo del producto: %s"
    const val FORMAT_RECEIPT_CATEGORY = "Categoria del producto: %s"
    const val FORMAT_RECEIPT_VALUE_TOTAL = "Valor total: %s"
    const val FORMAT_RECEIPT_VALUE_UNIT = "Valor por unidad: %s"
    const val FORMAT_RECEIPT_AMOUNT = "Cantidad: %s"
    const val FORMAT_RECEIPT_REFERENCE = "Referencia: %s"
    const val FORMAT_RECEIPT_NUMBER = "Numero: %s"

    //Format number
    fun formatNumber(phone: String): String {
        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        val number = phoneNumberUtil.parse("+57${phone}", "CO")
        return phoneNumberUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.NATIONAL)
    }


    fun getMonthName(monthNumber: Int): String {
        return when (monthNumber) {
            1 -> "Enero"
            2 -> "Febrero"
            3 -> "Marzo"
            4 -> "Abril"
            5 -> "Mayo"
            6 -> "Junio"
            7 -> "Julio"
            8 -> "Agosto"
            9 -> "Septiembre"
            10 -> "Octubre"
            11 -> "Noviembre"
            12 -> "Diciembre"
            else -> "El número de mes debe estar entre 1 y 12."
        }
    }
}