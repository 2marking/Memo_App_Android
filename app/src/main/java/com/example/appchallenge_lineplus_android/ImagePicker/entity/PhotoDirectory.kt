package com.example.appchallenge_lineplus_android.ImagePicker.entity

import java.util.*

class PhotoDirectory {
    var id: String? = null
    var coverPath: String? = null
    var name: String? = null
    var dateAdded: Long = 0
    private var photos: MutableList<Photo> =
        ArrayList()

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is PhotoDirectory) return false
        val directory = o
        return if (id != directory.id) false else name == directory.name
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    fun getPhotos(): MutableList<Photo> {
        return photos
    }

    fun setPhotos(photos: MutableList<Photo>) {
        this.photos = photos
    }

    val photoPaths: List<String?>
        get() {
            val paths: MutableList<String?> =
                ArrayList(photos.size)
            for (photo in photos) {
                paths.add(photo.path)
            }
            return paths
        }

    fun addPhoto(id: Int, path: String?) {
        photos.add(Photo(id, path))
    }
}