package com.kent.appbastos.validate

import com.google.android.material.textfield.TextInputLayout
import java.util.*

class ValidateEmpty {

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

}