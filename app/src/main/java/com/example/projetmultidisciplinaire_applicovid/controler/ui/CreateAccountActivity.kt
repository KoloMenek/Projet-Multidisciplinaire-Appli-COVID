package com.example.projetmultidisciplinaire_applicovid.controler.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.projetmultidisciplinaire_applicovid.R

class CreateAccountActivity : AppCompatActivity(), TextWatcher {

    private lateinit var mFirstName: EditText
    private lateinit var mLastName: EditText
    private lateinit var mDateBtn: Button
    private lateinit var mBirthPlace: EditText
    private lateinit var mStreet: EditText
    private lateinit var mNumber: EditText
    private lateinit var mZipCode: EditText
    private lateinit var mCity: EditText
    private lateinit var mValidateBtn: Button
    private lateinit var mBottomText: TextView
    private lateinit var mDateSetListener: DatePickerDialog.OnDateSetListener
    private var mYear: Int = -1
    private var mMonth: Int = -1
    private var mDay: Int = -1
    private var editList = listOf<EditText>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create_account)
        mBottomText = findViewById(R.id.text_bottom)
        mFirstName = findViewById(R.id.firstName_edit_txt)
        mLastName = findViewById(R.id.lastName_edit_txt)
        mDateBtn = findViewById(R.id.birthDate_edit_btn)
        mBirthPlace = findViewById(R.id.birthPlace_edit_txt)
        mStreet = findViewById(R.id.street_edit_txt)
        mNumber = findViewById(R.id.number_edit_txt)
        mZipCode = findViewById(R.id.zipCode_edit_txt)
        mCity = findViewById(R.id.city_edit_txt)
        mValidateBtn = findViewById(R.id.validate_btn)
        editList =
            listOf<EditText>(mFirstName, mLastName, mBirthPlace, mStreet, mNumber, mZipCode, mCity)

        val accountCreated: Boolean? =
            getSharedPreferences("UserData", Context.MODE_PRIVATE).getBoolean(
                "accountCreated",
                false
            )
        //if account was already created, prepares all the info with the info the user has provided
        if (accountCreated!!) {
            val preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
            mFirstName.setText(preferences.getString("fName", "FirstName"))
            mLastName.setText(preferences.getString("lName", "LastName"))
            mBirthPlace.setText(preferences.getString("bPlace", "Birth Place"))
            mStreet.setText(preferences.getString("street", "Street Name"))
            mNumber.setText(preferences.getString("num", "Number"))
            mZipCode.setText(preferences.getString("zipCode", "ZipCode"))
            mCity.setText(preferences.getString("city", "City"))
            mYear = preferences.getInt("bYear", 1)
            mMonth = preferences.getInt("bMonth", 1)
            mDay = preferences.getInt("bDay", 1)
            mDateBtn.text = "$mDay/$mMonth/$mYear"
            mValidateBtn.isEnabled = true
        }
        //opens DatePickerDialog for the user to pick birthday
        mDateBtn.setOnClickListener {
            var cal: Calendar = Calendar.getInstance()
            var year = cal.get(Calendar.YEAR)
            var month = cal.get(Calendar.MONTH)
            var day = cal.get(Calendar.DAY_OF_MONTH)

            var dpd: DatePickerDialog = DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,
                month,
                day
            )
            dpd.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dpd.show()
        }

        mDateSetListener =
            DatePickerDialog.OnDateSetListener { DatePicker, year, month, day ->
                mYear = year
                mMonth = month + 1
                mDay = day
                mDateBtn.text = "$mDay/$mMonth/$mYear"
                checkBtn()
            }

        for (e: EditText in editList) {
            e.addTextChangedListener(this)
        }
        //if all fields are completed, the user can save the data and exit this activity
        mValidateBtn.setOnClickListener {
            val preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("fName", mFirstName.text.toString())
            editor.putString("lName", mLastName.text.toString())
            editor.putString("bPlace", mBirthPlace.text.toString())
            editor.putString("street", mStreet.text.toString())
            editor.putString("zipCode", mZipCode.text.toString())
            editor.putString("num", mNumber.text.toString())
            editor.putString("city", mCity.text.toString())
            editor.putInt("bYear", mYear)
            editor.putInt("bMonth", mMonth)
            editor.putInt("bDay", mDay)
            editor.putBoolean("accountCreated", true)
            editor.apply()

            finish()
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun afterTextChanged(s: Editable?) {
        checkBtn()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
    //checks if the user completed the EditText and the DatePickerDialog
    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkEmpty(): Boolean {
        var isEmpty: Boolean = false
        if (mYear == Calendar.getInstance().get(Calendar.YEAR) || mYear == -1)
            return true
        for (e: EditText in editList) {
            if (e.text.isEmpty())
                return true

        }
        return isEmpty
    }
    //If the user completed the fields, it allows the user to click on the button. Otherwise, an error text shows.
    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkBtn() {
        mValidateBtn.isEnabled = !checkEmpty()
        if (!mValidateBtn.isEnabled)
            mBottomText.text = "Veuillez renseigner toutes vos informations..."
        else
            mBottomText.text = ""
    }
}