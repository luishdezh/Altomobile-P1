package com.example.prueba1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import okhttp3.*
import java.io.IOException
import okhttp3.OkHttpClient
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync
import org.json.JSONArray

class MainActivity : AppCompatActivity(){

    private val client = OkHttpClient()
    private val url = "https://flavioruben.herokuapp.com/data.json"
    var animals = ArrayList<ListItem>()
    lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var recyclerView = findViewById<RecyclerView>(R.id.main_list)
        val header = Header()
        animals.add(header)
        toast = Toast.makeText(this, "Sin conexión a Internet", Toast.LENGTH_SHORT)
        run(url)
        Thread.sleep(3000)

        recyclerView.adapter = AdapterAnimalArray(animals)
        recyclerView.layoutManager = LinearLayoutManager(this)
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
                    Thread.sleep(5000)
                    Log.d("MyTag", "Failure")
                    toast.show()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {

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
        val TYPE_ITEM = 0
        val TYPE_HEADER = 1
    }
}