package com.example.projetmultidisciplinaire_applicovid.modele

import java.time.LocalDate


class User(
    var usrLastName: String,
    var usrFirstName: String,
    var usrBirth: LocalDate,
    var usrZipCode: Int,
    var usrNumber: Int,
    var usrStreet: String,
    var usrCity: String
) {
    constructor() : this("","", LocalDate.now(),0,0,"","")

    var usrAddress: String = "$usrNumber $usrStreet $usrZipCode $usrCity"
}
