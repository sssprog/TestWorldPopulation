package com.sssprog.test2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class Page2Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page2, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.button)
    public void onButtonClick() {
        EventBus.getDefault().post(new GoToPage1Event());
    }

}
