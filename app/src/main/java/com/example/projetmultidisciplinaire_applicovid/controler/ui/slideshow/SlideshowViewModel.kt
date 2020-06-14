package com.example.projetmultidisciplinaire_applicovid.controler.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "\n\nAttention à l’automédication \n " +
                "CoVid-19 : quels sont les symptômes les plus fréquents ?\n\n" +
                "Fièvre : 88% ; \n" +
                "Toux sèche : 68% ; \n" +
                "Fatigue: 38% ;\n" +
                "Expectorations ou flegme épais des poumons : 33% ;\n" +
                "Essoufflement : 19% ; \n" +
                "Douleurs osseuses ou articulaires : 15% ;\n" +
                "Maux de gorge : 14% ;\n" +
                "Maux de tête : 14% ;\n" +
                "Frissons : 11% ;\n" +
                "Nausées ou vomissements : 5% ;\n" +
                "Nez bouché : 5% ;\n" +
                "Diarrhée : 4% ;\n" +
                "Toux de sang : 1% ;\n" +
                "Yeux gonflés : 1%.\n\n\n " +
                "Comment agir en cas de suspicion d’infection ?\n\nEn cas de suspicion d’infection, le patient est invité à rester chez lui et à appeler son médecin traitant " +
                "s’il ne présente que des symptômes légers, c’est-à-dire toux et fièvre. La téléconsultation est également possible et même à " +
                "privilégier : un décret a été publié le 10 mars afin d’ “assouplir les conditions de réalisation de la télémédecine”. " +
                "Le but : faire des professionnels de santé \"les premiers acteurs de la gestion sanitaire\" " +
                "de l’épidémie, et ainsi limiter les déplacements pour éviter de potentielles contaminations dans les salles d’attente des cabinets médicaux. " +
                "\n\nQuels symptômes doivent conduire à appeler le Samu ?\n" +
                "Les urgences seront également désengorgées ; seules les personnes présentant des difficultés respiratoires sont invitées à appeler le Samu (15) en vue d’un dépistage et d’une hospitalisation éventuels.\n" +
                "\n" +
                "Le docteur Jimmy Mohamed, médecin d'Europe 1, a donné à travers une vidéo quelques astuces permettant de savoir si la gêne respiratoire ressentie nécessite une prise en charge. La première consiste à mesurer sa fréquence respiratoire, c'est-à-dire le nombre d'inspirations réalisées en une minute. L'idéal étant que quelqu'un d'autre fasse la mesure afin de ne pas se perdre dans le comptage. Si l'on fait plus de 20 inspirations par minute, cela définit une respiration rapide (polypnée) et nécessite donc d'avoir \"un contact médical urgent pour évaluer la situation\". \n" +
                "\n" +
                "Deuxième astuce : calculer son score de Roth. Cela consiste à prendre une inspiration profonde, puis compter de 1 à 30 de façon claire mais rapide. Il convient ensuite de regarder jusqu'à combien on a réussi à compter et combien de temps on a mis pour compter. Si l'on a réussi à compter jusqu'à 10 au maximum ou si l'on a dû reprendre une inspiration avant 7 secondes, cela signifie que le taux d'oxygène est inférieur à 95% et nécessite donc d'appeler le 15.\n" +
                "\n" +
                "Le médecin précise qu'il ne s'agit pas de techniques fiables à 100% : elles peuvent toutefois donner une idée, orienter le patient sur ce qu'il doit faire."
    }
    val text: LiveData<String> = _text
}