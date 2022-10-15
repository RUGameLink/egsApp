package com.example.egsapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.logo)*/

        val gameList = ArrayList<Game>()

        val game1 = Game(
            "Test1",
            "test game description",
            "https://cdn1.epicgames.com/offer/d5241c76f178492ea1540fce45616757/egs-vault-w4-1920x1080_1920x1080-2df36fe63c18ff6fcb5febf3dd7ed06e"
        )
        gameList.add(game1)

        val game2 = Game(
            "Test2",
            "etwet ewtwet kew oewt ewot owket ko-wetkwet oweot kweotkwe tkwe- okwe ktew otewotoewtkewtk-ewt  k-ewtkot wet-kewtokw",
            "https://cdn1.epicgames.com/offer/d5241c76f178492ea1540fce45616757/egs-vault-w4-1920x1080_1920x1080-2df36fe63c18ff6fcb5febf3dd7ed06e"
        )
        gameList.add(game2)

        setCurrentAdapter(gameList)

        val gameFutList = ArrayList<Game>()

        val game11 = Game(
            "Test Future 1",
            "A mix between Portal, Zelda and Metroid. Explore, solve puzzles, beat up monsters, find secret upgrades and new abilities that help you reach new places. Playtime 12-25h.",
            "https://cdn1.epicgames.com/salesEvent/salesEvent/EGS_Supraland_SupraGames_S4_1200x1600-a6d4a615d97e0e784f93dbde64daa345"
        )
        gameFutList.add(game11)

        val game12 = Game(
            "Test Future 2",
            "A mix between Portal, Zelda and Metroid. Explore, solve puzzles, beat up monsters, find secret upgrades and new abilities that help you reach new places. Playtime 12-25h.",
            "https://cdn1.epicgames.com/salesEvent/salesEvent/EGS_Supraland_SupraGames_S4_1200x1600-a6d4a615d97e0e784f93dbde64daa345"
        )
        gameFutList.add(game12)

        setFutureAdapter(gameFutList)
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
}