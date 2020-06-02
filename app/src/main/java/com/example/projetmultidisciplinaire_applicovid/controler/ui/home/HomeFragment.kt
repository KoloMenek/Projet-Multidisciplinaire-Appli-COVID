package com.example.projetmultidisciplinaire_applicovid.controler.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.projetmultidisciplinaire_applicovid.R
import com.example.projetmultidisciplinaire_applicovid.controler.ui.MapsActivity
import com.example.projetmultidisciplinaire_applicovid.modele.User
import kotlinx.android.synthetic.main.content_main.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mGreetingText: TextView;
    private lateinit var mNameInput: EditText;
    private lateinit var mPlayButton: Button;
    private var user: User = User();
    private val REQUEST_QCM: Int = 187
    private lateinit var preferences : SharedPreferences;

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            mNameInput = findViewById<EditText>(R.id.);
            mPlayButton = findViewById<Button>(R.id.activity_main_play_btn);
            mNameInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    val usernameInput: String = mNameInput.text.toString().trim()
                    mPlayButton.isEnabled = usernameInput.isNotEmpty()
                }

                override fun afterTextChanged(s: Editable) {}
            })

            mPlayButton.setOnClickListener(){
                user.name = mNameInput.text.toString()
                val editor = preferences.edit()
                editor.putString("name", user.name)
                editor.apply()
                val monQCM = Intent(this@HomeFragment, MapsActivity::class.java)
                startActivityForResult(monQCM, REQUEST_QCM)

            }

        }

    }

}
