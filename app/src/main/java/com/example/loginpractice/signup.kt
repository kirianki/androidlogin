package com.example.loginpractice

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginpractice.databinding.ActivitySignupBinding // Import the binding class
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater) // Inflate binding
        setContentView(binding.root) // Set content view using binding

        //enableEdgeToEdge()

        //ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets -> // Use binding to access views
        //    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        //    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        //    insets
        //}
        firebaseDatabase= FirebaseDatabase.getInstance()
        databaseReference=firebaseDatabase.reference.child("users")

        binding.signupButton.setOnClickListener {
            val username = binding.signupUsername.text.toString() // Get username from EditText
            val password = binding.signupPassword.text.toString() // Get password from EditText

            if (username.isNotEmpty() && password.isNotEmpty()) {
                signUser(username, password) // Call the signUser function
            } else {
                Toast.makeText(this@signup, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            }
        }
        binding.loginRedirect.setOnClickListener {
            startActivity(Intent(this@signup, loginactivity::class.java))
            finish()
        }

    }

    private fun  signUser(username:String,password:String){
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                if(!datasnapshot.exists()){
                    val id=databaseReference.push().key
                    val userData=UserData(id,username,password)
                    databaseReference.child(id!!).setValue(userData)
                    Toast.makeText(this@signup,"Signup Successfully",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@signup,loginactivity::class.java))
                    finish()
                }
                else{
                    Toast.makeText(this@signup,"User Already Exists",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@signup,"Database Error: ${databaseError.message}",Toast.LENGTH_SHORT).show()
            }
        }
        )
    }
}