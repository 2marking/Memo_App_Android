package com.example.appchallenge_lineplus_android.ImagePicker.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appchallenge_lineplus_android.ImagePicker.entity.Photo;
import com.example.appchallenge_lineplus_android.ImagePicker.entity.PhotoDirectory;
import com.example.appchallenge_lineplus_android.ImagePicker.event.Selectable;

import java.util.ArrayList;
import java.util.List;

public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements Selectable {
    protected List<PhotoDirectory> photoDirectories;
    protected List<Photo> selectedPhotos;
    public int currentDirectoryIndex = 0;

    public SelectableAdapter() {
        photoDirectories = new ArrayList<>();
        selectedPhotos = new ArrayList<>();
    }

    @Override
    public boolean isSelected(Photo photo) {
        return getSelectedPhotos().contains(photo);
    }

    @Override
    public void toggleSelection(Photo photo) {
        if (selectedPhotos.contains(photo)) {
            selectedPhotos.remove(photo);
        } else {
            selectedPhotos.add(photo);
        }
    }

    @Override
    public int getSelectedItemCount() {
        return selectedPhotos.size();
    }

    public List<Photo> getCurrentPhotos() { return photoDirectories.get(currentDirectoryIndex).getPhotos(); }


    public List<String> getCurrentPhotoPaths() {
        List<String> currentPhotoPaths = new ArrayList<>(getCurrentPhotos().size());
        for (Photo photo : getCurrentPhotos()) currentPhotoPaths.add(photo.getPath());

        return currentPhotoPaths;
    }

    @Override
    public List<Photo> getSelectedPhotos() {
        return selectedPhotos;
    }

}