package com.example.prueba1

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainPresenter (private val view: MainContract.View) : MainContract.Presenter {
    override fun fetch(response: Int) {
        doAsync {
            Thread.sleep(4500)
            if (response==1) {
                uiThread {
                    view.showList()
                }
            } else {
                uiThread {
                    view.showNoList()
                }
            }
        }
    }
}