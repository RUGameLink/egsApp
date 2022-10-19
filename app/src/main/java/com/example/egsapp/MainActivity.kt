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

class MainActivity : AppCompatActivity() {
    private lateinit var statusText: TextView
    private lateinit var currentText: TextView
    private lateinit var futureText: TextView
    private val gameList = ArrayList<Game>()
    private val gameFutList = ArrayList<Game>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()



        val game1 = Game(
            "Test1",
            "test game description",
            "https://cdn1.epicgames.com/offer/d5241c76f178492ea1540fce45616757/egs-vault-w4-1920x1080_1920x1080-2df36fe63c18ff6fcb5febf3dd7ed06e",
            "current"
        )
        gameList.add(game1)

        val game2 = Game(
            "Test2",
            "etwet ewtwet kew oewt ewot owket ko-wetkwet oweot kweotkwe tkwe- okwe ktew otewotoewtkewtk-ewt  k-ewtkot wet-kewtokw",
            "https://cdn1.epicgames.com/offer/d5241c76f178492ea1540fce45616757/egs-vault-w4-1920x1080_1920x1080-2df36fe63c18ff6fcb5febf3dd7ed06e",
            "current"
        )
        gameList.add(game2)




        val game11 = Game(
            "Test Future 1",
            "A mix between Portal, Zelda and Metroid. Explore, solve puzzles, beat up monsters, find secret upgrades and new abilities that help you reach new places. Playtime 12-25h.",
            "https://cdn1.epicgames.com/salesEvent/salesEvent/EGS_Supraland_SupraGames_S4_1200x1600-a6d4a615d97e0e784f93dbde64daa345",
            "future"
        )
        gameFutList.add(game11)

        val game12 = Game(
            "Test Future 2",
            "A mix between Portal, Zelda and Metroid. Explore, solve puzzles, beat up monsters, find secret upgrades and new abilities that help you reach new places. Playtime 12-25h.",
            "https://cdn1.epicgames.com/salesEvent/salesEvent/EGS_Supraland_SupraGames_S4_1200x1600-a6d4a615d97e0e784f93dbde64daa345",
            "future"
        )
        gameFutList.add(game12)

        setStatus()
        setCurrentAdapter(gameList)
        setFutureAdapter(gameFutList)
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