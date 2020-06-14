package com.example.projetmultidisciplinaire_applicovid.controler.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.projetmultidisciplinaire_applicovid.R
import com.example.projetmultidisciplinaire_applicovid.controler.ui.MapsActivity
import com.example.projetmultidisciplinaire_applicovid.controler.ui.PdfActivity
import com.example.projetmultidisciplinaire_applicovid.modele.User


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mGreetingText: TextView
    private lateinit var mGreetingText2: TextView
    private lateinit var mMapsBtn: Button;
    private lateinit var mAttestation: Button;
    private var user: User = User();

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
        mGreetingText = view.findViewById(R.id.activity_main_greeting_txt)
        mMapsBtn = view.findViewById<Button>(R.id.activity_main_play_btn);
        mGreetingText2 = view.findViewById(R.id.activity_main_greeting_txt2)
        mAttestation = view.findViewById(R.id.activity_main_creerPdf)
        val userLName = activity!!.getSharedPreferences("UserData", Context.MODE_PRIVATE)
            .getString("lName", "LastName")
        val userFName = activity!!.getSharedPreferences("UserData", Context.MODE_PRIVATE)
            .getString("fName", "FirstName")
        mGreetingText.setText(
            "Bonjour $userFName $userLName \n" +
                    "Que voulez-vous faire ?"
        )


        mMapsBtn.setOnClickListener() {
            val myMaps = Intent(activity, MapsActivity::class.java)
            startActivity(myMaps)

        }
        mAttestation.setOnClickListener {
            val newActivity = Intent(activity, PdfActivity::class.java)
            startActivity(newActivity)
        }

    }

}

