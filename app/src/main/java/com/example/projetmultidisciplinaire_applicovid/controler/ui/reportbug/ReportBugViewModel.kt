package com.example.projetmultidisciplinaire_applicovid.controler.ui.reportbug

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportBugViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Report Bug Fragment"
    }
    val text: LiveData<String> = _text
}
