package com.example.e_val
import android.widget.Toast
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_admin_panel.*

class AdminPanelActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private var currentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        searchButton.setOnClickListener {
            val id = searchField.text.toString().trim()
            if (id.isEmpty()) return@setOnClickListener

            db.collection("permits").document(id).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        permitDetails.text = "Name: ${doc.getString("name")}\nStatus: ${doc.getString("status")}"
                        currentId = id
                        verifyButton.isEnabled = true
                    } else {
                        Toast.makeText(this, "Permit not found", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        verifyButton.setOnClickListener {
            currentId?.let { id ->
                val log = hashMapOf(
                    "studentId" to id,
                    "verifiedBy" to "Admin",
                    "timestamp" to System.currentTimeMillis()
                )
                db.collection("validation_logs").add(log)
                    .addOnSuccessListener { Toast.makeText(this, "Permit Verified", Toast.LENGTH_SHORT).show() }
                    .addOnFailureListener { Toast.makeText(this, "Failed to verify", Toast.LENGTH_SHORT).show() }
            }
        }
    }
}