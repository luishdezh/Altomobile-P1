package com.example.prueba1

interface MainContract {
    interface View {
        fun showProgress()
        fun showList()
        fun showNoList()
    }

    interface Presenter {
        fun fetch(response: Int)
    }
}
