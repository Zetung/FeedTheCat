package com.zetung.android.feedthecat

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.LocalTime

class MainFragmentView : Fragment(){

    private lateinit var feedButton: Button
    private lateinit var catImage: ImageView
    private lateinit var satietyTextView: TextView

    private var authFB = FirebaseAuth.getInstance()
    private var userIdFB = authFB.currentUser?.uid.toString()

    private var db = FirebaseDatabase.getInstance()
    private var refDb = db.getReference()

    private var allSession: Long = 0
    private var currentIndex = 0
    private var checkIndFifty = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        refDb.child("Users").child(userIdFB).child("allSession").get().addOnSuccessListener {
            allSession = it.value as Long
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_view, container, false)
        feedButton = view.findViewById(R.id.buttonFeed) as Button
        catImage = view.findViewById(R.id.imageViewCat) as ImageView
        satietyTextView = view.findViewById(R.id.textViewScore) as TextView
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        postUserSession()
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun postUserSession() {
        if (currentIndex != 0) {

            val dateMap = mutableMapOf<String, Any>()
            val scoreMap = mutableMapOf<String, Any>()
            dateMap["Date"] = "${LocalDate.now()} ${LocalTime.now()}"
            scoreMap["Score"] = currentIndex
            refDb.child("Users").child(userIdFB).child("Session$allSession").updateChildren(dateMap)
            refDb.child("Users").child(userIdFB).child("Session$allSession")
                .updateChildren(scoreMap)
            allSession++
            refDb.child("Users").child(userIdFB).child("allSession").setValue(allSession)

            setFragmentResult("requestLastSession",
                bundleOf("lastSession" to "${LocalDate.now()} ${LocalTime.now()}, Score: $currentIndex"))
        }



    }

    override fun onResume() {
        super.onResume()
        var out = getString(R.string.text_view_score,currentIndex)

        feedButton.setOnLongClickListener{
            catImage.setImageResource(R.drawable.pop_cat1)
            checkIndFifty = true
            false
        }

        feedButton.setOnClickListener{
            currentIndex++
            checkInd(currentIndex)
            out = getString(R.string.text_view_score,currentIndex)
            satietyTextView.setText(out)
            catImage.setImageResource(R.drawable.pop_cat2)
            checkIndFifty = false
        }
        satietyTextView.text = out

    }

    private fun checkInd(index:Int){
        if(index % 50 == 0){
            catImage.animate().apply{
                duration = 1000
                rotationYBy(360f)
            }
        }
        if (index % 50 == 0 && checkIndFifty){
            currentIndex+=23
            catImage.animate().apply{
                duration = 1000
                rotationXBy(360f)
            }
        }
    }

}