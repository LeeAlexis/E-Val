package com.example.e_val
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_val.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PermitActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permit)

        val name = findViewById<TextView>(R.id.studentName)
        val course = findViewById<TextView>(R.id.studentCourse)
        val status = findViewById<TextView>(R.id.permitStatus)

        val uid = auth.currentUser?.uid ?: return

        db.collection("permits").document(uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    name.text = "Name: ${doc.getString("name")}"
                    course.text = "Course: ${doc.getString("course")}"
                    status.text = "Status: ${doc.getString("status")}"
                } else {
                    Toast.makeText(this, "Permit not found", Toast.LENGTH_SHORT).show()
                }
            }
    }
}