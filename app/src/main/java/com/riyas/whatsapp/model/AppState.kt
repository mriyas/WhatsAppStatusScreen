package com.riyas.whatsapp.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.riyas.whatsapp.BR

data class AppState(var msg:String?): BaseObservable() {
    var state: Int=0
        @Bindable
        get() = field
        set(value) {
            if(field!=value){
                field = value
                notifyPropertyChanged(BR.state)

            }
        }

}