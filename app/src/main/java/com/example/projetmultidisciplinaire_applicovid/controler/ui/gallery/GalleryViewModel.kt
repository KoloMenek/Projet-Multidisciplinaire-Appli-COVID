package com.example.projetmultidisciplinaire_applicovid.controler.ui.gallery


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "\n\n- Éviter les contacts proches\n\n- Se laver fréquemment les mains\n\n- Éviter de se toucher les yeux, le nez et la bouche." +
                "\n\n- Respecter les règles d’hygiène respiratoire \n\n- Tenez-vous informé et suivez les conseils de votre médecin\n\n\n QUESTIONS : \n\n\n" +
                "Q : Faut-il éviter de se serrer la main à cause du nouveau coronavirus ?\n" +
                "\n" +
                "R : Oui. On peut contracter les virus respiratoires en serrant la main à quelqu’un puis en se touchant les yeux, le nez ou la bouche. Saluez d’un signe de la main ou de la tête, ou encore en vous inclinant.\n" +
                "\n" +
                "Q : Comment saluer une personne pour éviter d’attraper le nouveau coronavirus ?\n" +
                "\n" +
                "R : Le moyen le plus sûr de se saluer pour prévenir la COVID-19 est d’éviter les contacts physiques. On peut saluer d’un signe de la main ou de la tête, ou encore en s’inclinant.\n" +
                "\n" +
                "Q : Le port de gants en caoutchouc dans les lieux publics permet-il d’éviter l’infection par le nouveau coronavirus ?\n" +
                "\n" +
                "R : Non. Le fait de se laver les mains régulièrement protège mieux contre la COVID-19 que le port de gants en caoutchouc. " +
                "Le virus peut se trouver sur les gants et il y a un risque de contamination si vous vous touchez le visage avec les gants.\n\n"
    }
    val text: LiveData<String> = _text
}