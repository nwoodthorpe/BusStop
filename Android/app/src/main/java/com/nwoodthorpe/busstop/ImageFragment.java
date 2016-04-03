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

        String favData = "Bus to School|1368|1368-COWAN / WALACE|60|60 - NORTHVIEW ACRES|43.3841743|-80.29449|1000+Bus Home|1123|1123-U|200|200 - iXpress (To Ainslie)|43.47273|-80.54123|1000";

        ArrayList<FavRoute> rowArray = Serialization.deserialize(favData);

        ArrayAdapter adapter = new MenuListAdapter(getActivity(), 0, rowArray);

        // Link the data and our listview using the adapter.
        ListView listView = (ListView) layoutView.findViewById(R.id.list);
        listView.setAdapter(adapter);
        return layoutView;
    }
}