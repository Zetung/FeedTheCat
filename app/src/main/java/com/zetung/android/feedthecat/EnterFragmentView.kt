package com.zetung.android.feedthecat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EnterFragmentView : Fragment() {

    private lateinit var editTextLogin: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignUp: TextView
    private lateinit var buttonSignIn: Button

    private var authFB = FirebaseAuth.getInstance()
    private var refDb = FirebaseDatabase.getInstance().getReference()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_enter_view, container, false)
        editTextLogin = view.findViewById(R.id.editTextPersonName)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        buttonSignUp = view.findViewById(R.id.textViewPassword)
        buttonSignIn = view.findViewById(R.id.buttonSignIn)
        return view
    }

    override fun onResume(){
        super.onResume()

        buttonSignUp.setOnClickListener {
            if( editTextLogin.text.toString().isEmpty() || editTextPassword.text.toString().isEmpty()){
                Toast.makeText(activity,R.string.error_auth_empty,Toast.LENGTH_SHORT).show()
            } else {
                authFB.createUserWithEmailAndPassword(editTextLogin.text.toString(),editTextPassword.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        refDb.child("Users").child(authFB.currentUser?.uid.toString()).child("allSession").setValue(0)
                        coordinator().startMainFragment()
                    } else {
                        Toast.makeText(activity,R.string.error_auth_some,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        buttonSignIn.setOnClickListener {
            if(editTextLogin.text.toString().isEmpty() || editTextPassword.text.toString().isEmpty()){
                Toast.makeText(activity,R.string.error_auth_empty,Toast.LENGTH_SHORT).show()
            } else {
                authFB.signInWithEmailAndPassword(editTextLogin.text.toString(),editTextPassword.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        okEnter()
                    } else {
                        Toast.makeText(activity,R.string.error_auth_some,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun okEnter(){
        refDb.child("Users").child(authFB.currentUser?.uid.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val allSession = snapshot.child("allSession").value as Long
                val arrayOfResults = arrayListOf<String>()
                for (i in 0 until allSession) {
                    val strTemp =
                        snapshot.child("Session${i}").child("Date").value.toString() + ", Score: " +
                                snapshot.child("Session${i}").child("Score").value.toString()
                    arrayOfResults.add(strTemp)
                }
                setFragmentResult("requestEnterSession", bundleOf("Sessions" to arrayOfResults))
                coordinator().startMainFragment()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}