package com.kent.appbastos.model.validate

import com.google.android.material.textfield.TextInputLayout
import com.kent.appbastos.model.util.Keys
import java.util.*

class ValidateEmpty {

    fun validateMin(textGreaterThan: String, textLessThan: String, inputLayoutGreater: TextInputLayout, inputLayoutLess: TextInputLayout): Boolean{
        var validate = true
        val greaterThan = textGreaterThan.toFloat()
        val lessThan = textLessThan.toFloat()

        if (greaterThan <= lessThan){
            inputLayoutGreater.isErrorEnabled = true
            inputLayoutGreater.isErrorEnabled = true

            inputLayoutGreater.error = Keys.ERROR_GREATER_THAT_MIN
            inputLayoutLess.error = Keys.ERROR_GREATER

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
                inputLayout[num].error = Keys.ERROR_FIELD_EMPTY
                valid = false
            }else{
                inputLayout[num].isErrorEnabled = false
            }
        }
        return valid
    }

    fun valueUnit(value:String, inputLayout: TextInputLayout): Boolean{
        var validate = true
        if(value == "0"){
            inputLayout.isErrorEnabled = true
            inputLayout.error = Keys.ERROR_FIELD_ZERO
            validate = false
        }else{
            inputLayout.isErrorEnabled = false
        }
        return validate
    }
}