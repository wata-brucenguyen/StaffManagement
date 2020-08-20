package com.example.staffmanagement.View.Admin.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Admin.Home.Weather;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Weather> weatherArrayList;

    public WeatherAdapter(Context context, ArrayList<Weather> weatherArrayList) {
        this.context = context;
        this.weatherArrayList = weatherArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextViewName().setText(weatherArrayList.get(position).getName());
        Glide.with(context)
                .load(weatherArrayList.get(position).getLinkImage())
                .override(400)
                .into(holder.getImageViewWeather());
    }

    @Override
    public int getItemCount() {
        return weatherArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewWeather;
        private TextView textViewName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewWeather = itemView.findViewById(R.id.imageViewWeather);
            textViewName = itemView.findViewById(R.id.textViewWeatherName);
        }

        public ImageView getImageViewWeather() {
            return imageViewWeather;
        }

        public void setImageViewWeather(ImageView imageViewWeather) {
            this.imageViewWeather = imageViewWeather;
        }

        public TextView getTextViewName() {
            return textViewName;
        }

        public void setTextViewName(TextView textViewName) {
            this.textViewName = textViewName;
        }
    }
}
