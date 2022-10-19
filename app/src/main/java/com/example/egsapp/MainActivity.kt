package com.example.egsapp

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.egsapp.adapter.CurrentRecyclerAdapter
import com.example.egsapp.adapter.FutureRecyclerAdapter
import com.example.egsapp.database.DbManager
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.json.JSONTokener

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
    //    dbManager.deleteFromDb()

    //    insertData()

        setStatus()
        setCurrentAdapter(gameList)
        setFutureAdapter(gameFutList)
    }

    private fun insertData() {
        try {
            val thread = Thread {
                //Работа с api
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://free-epic-games.p.rapidapi.com/free")
                    .get()
                    .addHeader("X-RapidAPI-Key", "531597c006msh7b2eedadf7a590bp10af15jsn3ba9d7400d03")
                    .addHeader("X-RapidAPI-Host", "free-epic-games.p.rapidapi.com")
                    .build()

                val response = client.newCall(request).execute() //Отправка запроса в api
                val result = response.body()?.string() //Получение результатов в видо json файла
                var error = JSONObject(result).optString("message")
                if (error.isNotEmpty()) {
                    runOnUiThread {
                        statusText.text = "Конец света! Нет больше еды!"
                    }
                } else {
                    dbManager.deleteFromDb()
                    val jsonObject = JSONTokener(result).nextValue() as JSONObject
                    val jsonFreeGames = jsonObject.getJSONObject("freeGames")
                    val jsonArray = jsonFreeGames.getJSONArray("current")
                    val jsonArrayFuture = jsonFreeGames.getJSONArray("upcoming")
                    runOnUiThread {
                        dbManager.openDb()
                        for (i in 0 until jsonArray.length()) {
                            val title = jsonArray.getJSONObject(i).getString("title")
                            val desc = jsonArray.getJSONObject(i).getString("description")
                            val image = jsonArray.getJSONObject(i).getJSONArray("keyImages").getJSONObject(0).getString("url")
                            dbManager.insertToDb(Game(title, desc, image, "current"))
                        }
                        for (i in 0 until jsonArrayFuture.length()) {
                            val title = jsonArrayFuture.getJSONObject(i).getString("title")
                            val desc = jsonArrayFuture.getJSONObject(i).getString("description")
                            val image = jsonArrayFuture.getJSONObject(i).getJSONArray("keyImages").getJSONObject(0).getString("url")
                            dbManager.insertToDb(Game(title, desc, image, "future"))
                        }
                        dbManager.closeDb()
                    }
                }
            }
            thread.start() //Открытие потока
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
            Toast.makeText(this, "Обновляю данные...", Toast.LENGTH_SHORT).show()
            insertData()

            val handler = Handler()
            handler.postDelayed(Runnable {
                readData()

                setCurrentAdapter(gameList)
                setFutureAdapter(gameFutList)
            }, 5000)

            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}