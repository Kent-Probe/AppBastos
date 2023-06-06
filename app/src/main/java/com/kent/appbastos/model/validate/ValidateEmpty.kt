package com.kent.appbastos.model.validate

import com.google.android.material.textfield.TextInputLayout
import java.util.*

class ValidateEmpty {

    fun validateMin(textGreaterThan: String, textLessThan: String, inputLayoutGreater: TextInputLayout, inputLayoutLess: TextInputLayout): Boolean{
        var validate = true
        val greaterThan = textGreaterThan.toFloat()
        val lessThan = textLessThan.toFloat()

        if (greaterThan <= lessThan){
            inputLayoutGreater.isErrorEnabled = true
            inputLayoutGreater.isErrorEnabled = true

            inputLayoutGreater.error = "Error: debe ser mayor que la cantidad minima"
            inputLayoutLess.error = "Error: debe ser menor que la cantidad"

            validate = false
        }else{
            inputLayoutGreater.isErrorEnabled = false
            inputLayoutLess.isErrorEnabled = false
        }
        return validate
    }

    fun validate(texts: Vector<String>, inputLayout: Vector<TextInputLayout>): Boolean{

        var valid = true
        for ((num, text) in texts.withIndex()){
            if(text.isEmpty()){
                inputLayout[num].isErrorEnabled = true
                inputLayout[num].error = "Error: Campo Vacio"
                valid = false
            }else{
                inputLayout[num].isErrorEnabled = false
            }
        }
        return valid
    }

    fun validateEmptyOrNull(array: List<String>?): Boolean{
        var validate = true
        if (array == null || array.isEmpty()){
            validate = false
        }
        return validate
    }

    fun valueUnit(value:String, inputLayout: TextInputLayout): Boolean{
        var validate = true
        if(value == "0"){
            inputLayout.isErrorEnabled = true
            inputLayout.error = "Error: No puede ser 0"
            validate = false
        }else{
            inputLayout.isErrorEnabled = false
        }
        return validate
    }
}