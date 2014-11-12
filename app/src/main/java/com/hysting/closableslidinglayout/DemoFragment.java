package com.hysting.closableslidinglayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hysting.library.ClosableSlidingLayout;

import java.util.Random;

/**
 * Created by Hysting on 2014/11/12.
 */
public class DemoFragment extends Fragment {

    private final int[] colors = {Color.CYAN,Color.RED,Color.GRAY,Color.WHITE,Color.YELLOW,Color.MAGENTA};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_demo, container, false);
        ClosableSlidingLayout slideLayout = (ClosableSlidingLayout) layout.findViewById(R.id.slide_layout);
        View v = layout.findViewById(R.id.rl);
        v.setBackgroundColor(colors[new Random().nextInt(colors.length)]);
        Button btnNew = (Button) layout.findViewById(R.id.btn_new);

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction().add(R.id.container, new DemoFragment()).addToBackStack("").commit();
            }
        });

        slideLayout.setSlideListener(new ClosableSlidingLayout.SlideListener() {
            @Override
            public void onClosed() {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return layout;
    }
}
