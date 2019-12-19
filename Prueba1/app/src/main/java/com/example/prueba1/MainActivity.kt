package com.example.prueba1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import okhttp3.*
import java.io.IOException
import okhttp3.OkHttpClient
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.ViewAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONException

class MainActivity : AppCompatActivity(),  MainContract.View{



    private lateinit var switcher: ViewAnimator
    private lateinit var button: Button
    private lateinit var presenter: MainContract.Presenter
    private val client = OkHttpClient()
    private val url = "https://flavioruben.herokuapp.com/data.json"
    private var mResponse: Int = 1
    var animals = ArrayList<ListItem>()
    lateinit var toast: Toast

    companion object {
        const val SHOW_LOAD = 0
        const val SHOW_LIST = 1
        const val SHOW_NO_LIST = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switcher = findViewById(R.id.switcher)
        button = findViewById(R.id.button)


        var recyclerView = findViewById<RecyclerView>(R.id.main_list)
        val header = Header()
        animals.add(header)
        toast = Toast.makeText(this, "Sin conexión a Internet", Toast.LENGTH_SHORT)
        run(url)
        recyclerView.adapter = AdapterAnimalArray(animals)
        recyclerView.layoutManager = LinearLayoutManager(this)

        presenter = MainPresenter(this@MainActivity)
        presenter.fetch(mResponse)

        button.setOnClickListener{
            restart()
        }

    }

    private fun restart(){
        val intent = Intent( this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showProgress() {
        switcher.displayedChild = SHOW_LOAD
    }

    override fun showList() {
        switcher.displayedChild = SHOW_LIST
    }

    override fun showNoList() {
        switcher.displayedChild = SHOW_NO_LIST
    }

    private fun run(url: String){

            val request = Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build()

            client.newCall(request).enqueue(object : Callback {
                @Throws(IOException::class)
                override fun onFailure(call: Call, e: IOException) {
                    doAsync {
                        uiThread{
                            mResponse = SHOW_NO_LIST
                            presenter.fetch(mResponse)
                        }
                    }
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    doAsync {
                        uiThread{
                            mResponse = SHOW_LIST
                        }
                        val data = response.body?.string().toString()
                        val json = JSONArray(data)

                        for (i in 0 until json.length()) {
                            val obj = json.getJSONObject(i)
                            val animal = Animal(
                                obj.getString("name"),
                                Integer.parseInt(obj.getString("life")),
                                Integer.parseInt(obj.getString("id")),
                                obj.getString("pictureURL")
                            )
                            animals.add(animal)
                        }
                    }

                }
            })
    }

}

class Animal : ListItem{
    private var name: String
    private var life: Int = 0
    private var id: Int = 0
    private var pictureURL: String

    constructor(name: String, life: Int, id: Int, pictureURL: String) {
        this.name = name
        this.life = life
        this.id = id
        this.pictureURL = pictureURL
    }

    fun getName():String{
        return this.name
    }

    fun getLife():Int{
        return this.life
    }

    fun getPictureURL():String{
        return this.pictureURL
    }

    override fun itemType(): Int{
        return ListItem.TYPE_ITEM
    }
}

class Header : ListItem {
    override fun itemType(): Int{
        return ListItem.TYPE_HEADER
    }
}

interface ListItem {

    fun itemType(): Int

    companion object {
        const val TYPE_ITEM = 0
        const val TYPE_HEADER = 1
    }
}
