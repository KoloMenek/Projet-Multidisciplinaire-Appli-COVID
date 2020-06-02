package com.example.projetmultidisciplinaire_applicovid.controler.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.projetmultidisciplinaire_applicovid.R
import com.example.projetmultidisciplinaire_applicovid.modele.User

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

    }
}
