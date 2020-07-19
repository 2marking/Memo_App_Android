package com.example.appchallenge_lineplus_android.ImagePicker.entity

class Photo {
    var id = 0
    var path: String? = null

    constructor(id: Int, path: String?) {
        this.id = id
        this.path = path
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is Photo) return false
        return id == o.id
    }

    override fun hashCode(): Int {
        return id
    }
}