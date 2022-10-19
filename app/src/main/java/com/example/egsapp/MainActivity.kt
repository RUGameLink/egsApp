package com.example.egsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.egsapp.adapter.CurrentRecyclerAdapter
import com.example.egsapp.adapter.FutureRecyclerAdapter
import com.example.egsapp.database.DbManager
import okhttp3.OkHttpClient
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var statusText: TextView
    private lateinit var currentText: TextView
    private lateinit var futureText: TextView

    private val gameList = ArrayList<Game>()
    private val gameFutList = ArrayList<Game>()

    private val dbManager = DbManager(this) //Инициализация бд-менеджера

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        readData()
        dbManager.deleteFromDb()

    //    insertData()

        setStatus()
        setCurrentAdapter(gameList)
        setFutureAdapter(gameFutList)
    }

    private fun insertData() {
        try {
//            val thread = Thread{
//                //Работа с api
//                val client = OkHttpClient()
//            }
            dbManager.openDb()

            dbManager.closeDb()
        }
        catch (ex: Exception){
            println(ex)
        }
    }

    private fun init(){
        statusText = findViewById(R.id.statusText)
        futureText = findViewById(R.id.futureText)
        currentText = findViewById(R.id.currentText)
    }

    private fun setStatus() {
        if (gameList.isEmpty()){
            statusText.visibility = View.VISIBLE
            currentText.visibility = View.INVISIBLE
            futureText.visibility = View.INVISIBLE
        }
        else{
            statusText.visibility = View.INVISIBLE
            currentText.visibility = View.VISIBLE
            futureText.visibility = View.VISIBLE
        }
    }

    private fun readData(){
        try {
            dbManager.openDb()
            val tempData = dbManager.readDbDataFromDb()
            for (i in 0 .. tempData.size){
                if (tempData[i].status.equals("current")){
                    gameList.add(tempData[i])
                }
                else{
                    gameFutList.add(tempData[i])
                }
            }
            dbManager.closeDb()
        }
        catch (ex: Exception){
            println(ex)
        }

    }


    private fun setCurrentAdapter(game: ArrayList<Game>){
        val recyclerView: RecyclerView = findViewById(R.id.currentView) //Подвязка ресайклера к объекту
        val linearLayoutManager = LinearLayoutManager(applicationContext) //Подготовка лайаут менеджера
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager //Инициализация лайаут менеджера
        recyclerView.adapter = CurrentRecyclerAdapter(game!!) //внесение данных из листа в адаптер (заполнение данными)
    }

    private fun setFutureAdapter(gameFut: ArrayList<Game>){
        val recyclerView: RecyclerView = findViewById(R.id.futureView) //Подвязка ресайклера к объекту
        val linearLayoutManager = LinearLayoutManager(applicationContext) //Подготовка лайаут менеджера
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager //Инициализация лайаут менеджера
        recyclerView.adapter = FutureRecyclerAdapter(gameFut!!) //внесение данных из листа в адаптер (заполнение данными)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_refresh -> {
            Toast.makeText(this, "Обновляю данные", Toast.LENGTH_SHORT).show()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}