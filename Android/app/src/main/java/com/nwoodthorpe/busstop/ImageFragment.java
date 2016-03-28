package com.nwoodthorpe.busstop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ImageFragment extends Fragment {
    int fragVal;

    static ImageFragment init(int val) {
        ImageFragment truitonFrag = new ImageFragment();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        truitonFrag.setArguments(args);
        return truitonFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.activity_menu, container,
                false);
        ArrayList<BusRow> rowArray = new ArrayList<>();

        String favData = "60 Near Home-3-60%200 Conestoga-7-7A%7 Outside DC-4-7C";

        String[] favorites = favData.split("%");
        System.out.println(favorites.length);
        for(int i = 0; i<favorites.length; i++){
            System.out.println("ONE ITERATION");

            String[] splitFavorites = favorites[i].split("-");
            System.out.println(splitFavorites[0] + " " + splitFavorites[1]);
            rowArray.add(new BusRow(splitFavorites[0], splitFavorites[1], splitFavorites[2]));
        }

        ArrayAdapter adapter = new MenuListAdapter(getActivity(), 0, rowArray);

        // Link the data and our listview using the adapter.
        ListView listView = (ListView) layoutView.findViewById(R.id.list);
        listView.setAdapter(adapter);
        return layoutView;
    }
}