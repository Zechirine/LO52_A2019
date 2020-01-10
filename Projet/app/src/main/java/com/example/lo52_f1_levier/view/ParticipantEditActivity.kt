package com.example.lo52_f1_levier.view

import android.app.Activity
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.lo52_f1_levier.DAO.CoureurDao
import com.example.lo52_f1_levier.R
import com.example.lo52_f1_levier.model.Coureur
import kotlinx.android.synthetic.main.activity_paticipant_edit.*

class ParticipantEditActivity : AppCompatActivity() {

    var runnerId : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paticipant_edit)

        // Get the value from the intent
        runnerId = intent.getIntExtra("runnerId", -1)

        // Initialize the dao
        val coureurDao = CoureurDao(this)

        var coureurNumc = -1

        // Get the runner data
        if(runnerId != -1){
            var res : Cursor? = coureurDao.getCoureurByID(runnerId)
            res?.moveToFirst()

            firstName.setText(res?.getString(res?.getColumnIndexOrThrow(Coureur.CoureurTable.CNAME)))
            lastName.setText(res?.getString(res?.getColumnIndexOrThrow(Coureur.CoureurTable.SURNAME)))
            coureurNumc = res?.getInt(res?.getColumnIndexOrThrow(Coureur.CoureurTable.NUMC))!!
        }

        btn_addParticipant.setOnClickListener{
            if(firstName.text.toString() != "" && lastName.text.toString() != ""){
                val coureurDao = CoureurDao(this)
                coureurDao.updateCoureurByID(runnerId,coureurNumc,
                    firstName.text.toString(), lastName.text.toString()) // Update the runner data
                Toast.makeText(this, "Modification enregistrée", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            }
            else{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Attention")
                builder.setMessage("Veuillez bien remplir toutes les informations nécessaires.")
                builder.show()
            }
        }

        btn_cancel.setOnClickListener {
            this.finish()
        }
    }
}
