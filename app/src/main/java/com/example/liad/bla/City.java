package com.example.liad.bla;


public class City {

    private String name, temp, description;
    private int descriptionImage;

    public City(String name, String temp, String description) {
        this.name = name;
        this.temp = temp;
        this.description = description;
    }

    public String getName() {
        return name;
    }


    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        switch (description) {
            case "Clouds":
                this.descriptionImage = R.drawable.cloudy;
                break;
            case "Sunny":
                this.descriptionImage = R.drawable.sun;
                break;
            case "Rainy":
                this.descriptionImage = R.drawable.rain;
                break;
            case "Clear":
                this.descriptionImage = R.drawable.sun;
                break;
            default:
                this.descriptionImage = R.drawable.sky;

        }
    }

    public int getDescImage() {
        return descriptionImage;
    }
}
