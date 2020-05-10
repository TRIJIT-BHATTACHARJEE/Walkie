package com.reg.user.regis;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by USER on 17-07-2017.
 */

public class Tab3Fragment extends Fragment {

    ListView lv;
    String[] items;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3fragment, container, false);

        lv = (ListView) view.findViewById(R.id.listView);

        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());

        items = new String[mySongs.size()];


        for (int i = 0; i < mySongs.size(); i++) {

            //toast(mySongs.get(i).getName().toString());

            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");

        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), R.layout.song_layout, R.id.textView, items);

        lv.setAdapter(adp);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(getActivity(), Player.class).putExtra("pos", position).putExtra("songlist", mySongs));

            }

        });



        return view;
    }

    private ArrayList<File> findSongs(File root) {
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {

                al.addAll(findSongs(singleFile));

            } else {

                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {

                    al.add(singleFile);

                }

            }


        }
        return al;
    }

    public void toast(String text) {

        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();

    }

}
