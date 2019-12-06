package com.example.prueba1

import android.content.Context
import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Picasso
import java.nio.file.Files.size

class AnimalHolder(view:View): RecyclerView.ViewHolder(view){
    private val name: TextView = view.findViewById(R.id.animal_name)
    private val pic: ImageView = view.findViewById(R.id.animal_image)
    private var rm = Glide.with(view)

    fun bind(aName:String, aImg:String){

        var uri = Uri.parse(aImg)
        if(aImg == ""){
            uri = Uri.parse("http://s3.amazonaws.com/37assets/svn/765-default-avatar.png")
        }

        name.text = aName
        rm.load(uri).apply(RequestOptions.circleCropTransform()).into(pic)
    }
}

class HeaderHolder(view: View) : RecyclerView.ViewHolder(view)

class AdapterAnimalArray(var animal: ArrayList<ListItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var life: ConstraintLayout
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.list_header, parent, false)
            return HeaderHolder(v)
        } else if (viewType == 0) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.list_animal, parent, false)
            life = v.findViewById(R.id.animal_item)
            context = parent.context

            return AnimalHolder(v)
        }
        throw RuntimeException("there is no type that matches the type $viewType + make sure your using types correctly")
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AnimalHolder) {
            val a = animal[position] as Animal
            holder.bind(a.getName(),a.getPictureURL())
            life.setOnClickListener {
                val toast = Toast.makeText(context, "Life: " + a.getLife(), Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    override fun getItemCount(): Int = animal.size

    override fun getItemViewType(position: Int): Int {
        return animal[position].itemType()
    }

}