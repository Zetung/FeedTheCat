package com.zetung.android.feedthecat

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.os.bundleOf
import com.google.firebase.auth.FirebaseAuth
import com.zetung.android.feedthecat.R.id.*

class MainActivity : AppCompatActivity(), CoordInterface {

    private lateinit var menuItemsShow: Array<MenuItem>
    private var authFB = FirebaseAuth.getInstance()
    private var shareResult = 0

    private var arrOfResults: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.setFragmentResultListener("requestEnterSession",this){
                _, bundle ->
            arrOfResults = bundle.getStringArrayList("Sessions")!!
        }
        supportFragmentManager.setFragmentResultListener("requestLastSession",this){
                _, bundle ->
            arrOfResults.add(bundle.getString("lastSession")!!)
        }
        supportFragmentManager.setFragmentResultListener("shareSession",this){
                _, bundle ->
            shareResult = bundle.getInt("shareScore")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        if (menu != null) {
            menuItemsShow = arrayOf(
                menu.findItem(new_game_settings),
                menu.findItem(results_settings),
                menu.findItem(sign_out_settings),
                menu.findItem(share_settings))
        }
        start()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            new_game_settings -> startMainFragment()
            developer_settings -> Toast.makeText(this,R.string.developer_info, Toast.LENGTH_SHORT).show()
            share_settings -> shareScore()
            results_settings -> startResultsFragment()
            sign_out_settings -> signOutFB()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        signOutFB()
        super.onDestroy()
    }

    fun shareScore() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Im feed my cat to score: $shareResult")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun signOutFB(){
        authFB.signOut()
        startEnterFragment()
    }

    override fun startEnterFragment() {

        for(item in menuItemsShow){
            item.isVisible = false
        }
        supportFragmentManager
            .beginTransaction()
            .replace(container, EnterFragmentView())
            .commit()
    }

    override fun startMainFragment(){

        for(item in menuItemsShow){
            item.isVisible = true
        }

        supportFragmentManager
            .beginTransaction()
            .replace(container, MainFragmentView())
            .commit()

    }

    override fun startResultsFragment() {

        for(item in menuItemsShow){
            item.isVisible = true
        }

        val binder = bundleOf("resultsSession" to arrOfResults)
        val resultsFragmentView = ResultsFragmentView()
        resultsFragmentView.arguments = binder

        supportFragmentManager
            .beginTransaction()
            .replace(container, resultsFragmentView)
            .commit()
    }

    override fun start() {
        startEnterFragment()
    }

    override fun finish() {

    }
}