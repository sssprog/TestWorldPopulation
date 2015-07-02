package com.sssprog.test2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Page1Fragment extends Fragment {

    @InjectView(R.id.image)
    ImageView image;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.description)
    TextView description;

    private DataLoader loader;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loader = DataLoader.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page1, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loader.loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loader.attach(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        loader.detach();
    }

    void onDataLoaded(Country country) {
        title.setText(country.country);
        description.setText(getString(R.string.population, country.population));
        if (country.flagUrl != null) {
            Picasso.with(getActivity()).load(country.flagUrl).into(image);
        }
    }
}
