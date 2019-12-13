package com.oolivares.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Recetas implements Parcelable {
    private int id;
    private String name;
    private String ingredients;
    private String steps;
    private int servings;
    private String image;

    public Recetas(int _id, String _name, String _ingredients , String _steps, int _servings, String _image){
        id = _id;
        name = _name;
        ingredients = _ingredients;
        steps = _steps;
        servings = _servings;
        image = _image;
    }

    protected Recetas(Parcel in) {
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        image = in.readString();
        ingredients = in.readString();
        steps = in.readString();
    }

    public static final Creator<Recetas> CREATOR = new Creator<Recetas>() {
        @Override
        public Recetas createFromParcel(Parcel in) {
            return new Recetas(in);
        }

        @Override
        public Recetas[] newArray(int size) {
            return new Recetas[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getServings() {
        return servings;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getSteps() {
        return steps;
    }


    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(servings);
        parcel.writeString(image);
        parcel.writeString(ingredients);
        parcel.writeString(steps);
    }
}
