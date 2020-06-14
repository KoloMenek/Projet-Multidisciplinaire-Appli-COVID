package com.example.projetmultidisciplinaire_applicovid.controler.ui

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.projetmultidisciplinaire_applicovid.R
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PdfActivity : AppCompatActivity() {
    private lateinit var btnTrav: Button
    private lateinit var btnCour: Button
    private lateinit var btnSant: Button
    private lateinit var btnFam: Button
    private lateinit var btnQuot: Button
    private lateinit var btnJudi: Button
    private lateinit var btnMission: Button
    private val STORAGE_CODE: Int = 100
    private lateinit var userFn: String
    private lateinit var userLn: String
    private lateinit var userBd: String
    private lateinit var userBdPlace: String
    private lateinit var userAddress: String
    private lateinit var userCity: String
    private var motifTravail: String = " "
    private var motifCourse: String = " "
    private var motifSante: String = " "
    private var motifFamilial: String = " "
    private var motifQuot: String = " "
    private var motifJudi: String = " "
    private var motifMission: String = " "
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private val channelId = "com.example.projetmultidisciplinaire_applicovid.controler.ui"
    private val description = "File created notification"
    private var mFilePath:String = ""

//Checks if the application has the needed permissions for creating a pdf file (Write / Read)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkPerm() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //Permission was not granted
                val permission = arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                requestPermissions(permission, STORAGE_CODE)
            } else {
                //Permission already granted
                createPdf()
            }
        } else {
            //System OS < Marshmallow, call createPDF() method
            createPdf()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        btnTrav = findViewById(R.id.motifTravail)
        btnCour = findViewById(R.id.motifCourses)
        btnSant = findViewById(R.id.motifSante)
        btnFam = findViewById(R.id.motifFamilial)
        btnQuot = findViewById(R.id.motifDepQuo)
        btnJudi = findViewById(R.id.motifJudiciaire)
        btnMission = findViewById(R.id.motifMission)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
//Gets user data from sharedPreferences
        userFn = preferences.getString("fName", "FirstName")!!
        userLn = preferences.getString("lName", "LastName")!!
        userBd = preferences.getInt("bDay", 1).toString()!! + "/" + preferences.getInt("bMonth", 1)
            .toString()!! + "/" + preferences.getInt("bYear", 1).toString()!!
        userBdPlace = preferences.getString("bPlace", "Birth Place")!!
        userAddress = preferences.getString("num", "Number")!! + " " + preferences.getString(
            "street",
            "Street Name"
        )!! + " " + preferences.getString(
            "zipCode",
            "ZipCode"
        )!! + " " + preferences.getString("city", "City")!!
        userCity = preferences.getString("city", "City")!!

        btnTrav.setOnClickListener {
            motifTravail = "X"
            checkPerm()
        }
        btnCour.setOnClickListener {
            motifCourse = "X"
            checkPerm()
        }
        btnSant.setOnClickListener {
            motifSante = "X"
            checkPerm()
        }
        btnFam.setOnClickListener {
            motifFamilial = "X"
            checkPerm()
        }
        btnQuot.setOnClickListener {
            motifQuot = "X"
            checkPerm()
        }
        btnJudi.setOnClickListener {
            motifJudi = "X"
            checkPerm()
        }
        btnMission.setOnClickListener {
            motifMission = "X"
            checkPerm()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createPdf() {
        //create object of Document class
        val mDoc = Document()
        //pdf file name
        var mFileName = "Attestation_" + userLn + "_" + userFn + "_"
        mFileName += SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(System.currentTimeMillis())

        //pdf file path
            mFilePath =
            Environment.getExternalStorageDirectory().toString() + "/" + mFileName + ".pdf"
        try {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))

            //Open the document for writing
            mDoc.open()

            //Add author
            mDoc.addAuthor("$userFn $userLn")

            //Custom Text String
            val attestation1 =
                "En application de l’article 3 du décret du 23 mars 2020 prescrivant les mesures générales nécessaires pour faire face à l’épidémie de Covid19 dans le cadre de l’état d’urgence sanitaire"
            val attestation2 = "Mme/M. : $userLn $userFn\n" +
                    "Né(e) le : $userBd \n" +
                    "A : $userBdPlace \n" +
                    "Demerant : $userAddress"


            //Write Document
            var font: Font = Font()
            font.setFamily("Arial")
            font.size = 11F

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")
            val formatted = current.format(formatter)
            var parag1: Paragraph = Paragraph()
            parag1.add("ATTESTATION DE DÉPLACEMENT DÉROGATOIRE")
            parag1.alignment = Element.ALIGN_CENTER
            parag1.font = font
            mDoc.add(parag1)
            mDoc.add(Chunk.NEWLINE)
            var parag2: Paragraph = Paragraph(attestation1)
            parag2.alignment = Element.ALIGN_CENTER
            parag2.font = font
            mDoc.add(parag2)
            mDoc.add(Chunk.NEWLINE)
            mDoc.add(Paragraph("Je sousigné(e),"))
            mDoc.add(Chunk.NEWLINE)
            mDoc.add(Paragraph(attestation2))
            mDoc.add(Chunk.NEWLINE)
            mDoc.add(Paragraph("certifie que mon déplacement est lié au motif suivant (cocher la case) autorisé par l’article 3 du décret du 23 mars 2020 prescrivant les mesures générales nécessaires pour faire face à l’épidémie de Covid19 dans le cadre de l’état d’urgence sanitaire :"))
            mDoc.add(Chunk.NEWLINE)
            mDoc.add(Paragraph("[$motifTravail] Déplacements entre le domicile et le lieu d’exercice de l’activité professionnelle, lorsqu’ils sont indispensables à l’exercice d’activités ne pouvant être organisées sous forme de télétravail ou déplacements professionnels ne pouvant être différés. "))
            mDoc.add(Chunk.NEWLINE)
            mDoc.add(Paragraph("[$motifCourse] Déplacements pour effectuer des achats de fournitures nécessaires à l’activité professionnelle et des achats de première nécessité dans des établissements dont les activités demeurent autorisées (liste sur gouvernement.fr). "))
            mDoc.add(Chunk.NEWLINE)
            mDoc.add(Paragraph("[$motifSante] Consultations et soins ne pouvant être assurés à distance et ne pouvant être différés ; consultations et soins des patients atteints d'une affection de longue durée."))
            mDoc.add(Chunk.NEWLINE)
            mDoc.add(Paragraph("[$motifFamilial] Consultations et soins ne pouvant être assurés à distance et ne pouvant être différés ; consultations et soins des patients atteints d'une affection de longue durée."))
            mDoc.add(Chunk.NEWLINE)
            mDoc.add(Paragraph("[$motifQuot] Déplacements brefs, dans la limite d'une heure quotidienne et dans un rayon maximal d'un kilomètre autour du domicile, liés soit à l'activité physique individuelle des personnes, à l'exclusion de toute pratique sportive collective et de toute proximité avec d'autres personnes, soit à la promenade avec les seules personnes regroupées dans un même domicile, soit aux besoins des animaux de compagnie. "))
            mDoc.add(Chunk.NEWLINE)
            mDoc.add(Paragraph("[$motifJudi] Convocation judiciaire ou administrative."))
            mDoc.add(Chunk.NEWLINE)
            mDoc.add(Paragraph("[$motifMission] Participation à des missions d’intérêt général sur demande de l’autorité administrative."))
            mDoc.add(Chunk.NEWLINE)
            mDoc.add(
                Paragraph(
                    "Fait à : $userCity \n" +
                            "Le : $formatted\n" +
                            "Signature : $userFn $userLn"
                )
            )

            //Close Document
            mDoc.close()

            //show path to document
            Toast.makeText(this, "$mFileName.pdf\n is saved to\n$mFilePath", Toast.LENGTH_SHORT).show()
            createNotification()
            finish()
        } catch (e: Exception) {
            //if anything goes wrong causing exception, get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
    //Function that creates a notification that allows the user to open the pdf file after clicking on it
    private fun createNotification(){
        //prepares the file to be opened
        val selectedUri: Uri =
            Uri.parse(mFilePath)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(selectedUri, "application/pdf") // here we set correct type for PDF
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        //creates the notification
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)
                .setContentTitle("Création de l'attestation")
                .setContentText("Cliquez pour ouvrir le fichier PDF")
                .setSmallIcon(R.drawable.ic_baseline_arrow_downward_24)
                .setContentIntent(pendingIntent)
        }else{
            builder = Notification.Builder(this, channelId)
                .setContentTitle("Création de l'attestation")
                .setContentText("Cliquez pour ouvrir le fichier PDF")
                .setSmallIcon(R.drawable.ic_baseline_arrow_downward_24)
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234,builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    //checks if permissions is granted if not there is a toast
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted, call createPdf()
                    createPdf()
                } else {
                    //permission from popup was denied, show error message
                    Toast.makeText(this, "Permission denied... !", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}