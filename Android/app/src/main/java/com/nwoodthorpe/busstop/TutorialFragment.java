package com.nwoodthorpe.busstop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TutorialFragment extends Fragment {
    int fragVal;
    View layoutView;

    static TutorialFragment init(int val) {
        TutorialFragment truitonFrag = new TutorialFragment();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        truitonFrag.setArguments(args);
        return truitonFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 0;
    }

    public void startAnimation() {
        switch (fragVal) {
            case 0:
                TextView welcomeText = (TextView) layoutView.findViewById(R.id.welcomeLabel);
                TextView secondText = (TextView) layoutView.findViewById(R.id.secondLabel);
                TextView swipeText = (TextView) layoutView.findViewById(R.id.swipeLabel);

                AlphaAnimation welcomeFadeIn = new AlphaAnimation(0.0f, 1.0f);
                AlphaAnimation secondFadeIn = new AlphaAnimation(0.0f, 1.0f);
                AlphaAnimation swipeFadeIn = new AlphaAnimation(0.0f, 1.0f);

                welcomeText.startAnimation(welcomeFadeIn);
                secondText.startAnimation(secondFadeIn);
                swipeText.startAnimation(swipeFadeIn);

                welcomeFadeIn.setDuration(1500);
                welcomeFadeIn.setFillAfter(true);
                welcomeFadeIn.setStartOffset(800);

                secondFadeIn.setDuration(1500);
                secondFadeIn.setFillAfter(true);
                secondFadeIn.setStartOffset(1000 + welcomeFadeIn.getStartOffset());

                swipeFadeIn.setDuration(1500);
                swipeFadeIn.setFillAfter(true);
                swipeFadeIn.setStartOffset(1000 + secondFadeIn.getStartOffset());
                break;

            case 1:
                RelativeLayout backgroundTint = (RelativeLayout) layoutView.findViewById(R.id.backgroundTint);
                RelativeLayout dialog = (RelativeLayout) layoutView.findViewById(R.id.dialog);

                AlphaAnimation backgroundFade = new AlphaAnimation(0.0f, 1.0f);
                AlphaAnimation dialogFade = new AlphaAnimation(0.0f, 1.0f);

                backgroundTint.startAnimation(backgroundFade);
                dialog.startAnimation(dialogFade);

                backgroundFade.setDuration(1500);
                backgroundFade.setFillAfter(true);
                backgroundFade.setStartOffset(1500);

                dialogFade.setDuration(1000);
                dialogFade.setFillAfter(true);
                dialogFade.setStartOffset(2000);
                break;
            default:
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        switch(fragVal) {
            case 0:
                layoutView = inflater.inflate(R.layout.activity_tutorial_intro, container,
                        false);
                startAnimation();
                break;
            case 1:
            case 2:
                if(fragVal==1)
                    layoutView = inflater.inflate(R.layout.activity_tutorial_menu, container,
                        false);
                else
                    layoutView = inflater.inflate(R.layout.activity_tutorial_menu_2, container, false);

                final FavRoute favoriteA = new FavRoute(0,0,"Bus To Mall", "", "60", "", "", 1, 20);
                final FavRoute favoriteB = new FavRoute(0, 0, "Waterloo Bus", "", "200", "", "", 0, 290);
                ArrayList<FavRoute> favorites = new ArrayList<FavRoute>(){{
                    add(favoriteA);
                    add(favoriteB);
                }};

                final ArrayAdapter adapter = new TutorialMenuListAdapter(getContext(), 0, favorites);

                // Link the data and our listview using the adapter.
                ListView listView = (ListView) layoutView.findViewById(R.id.list);

                listView.setAdapter(adapter);
                break;

            case 3:
                layoutView = inflater.inflate(R.layout.activity_tutorial_finished, container,
                        false);

                RelativeLayout layout = (RelativeLayout) layoutView.findViewById(R.id.layout);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                break;
            default:
                break;

        }

        return layoutView;
    }
}