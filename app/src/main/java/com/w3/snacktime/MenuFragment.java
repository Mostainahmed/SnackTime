package com.w3.snacktime;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    RecyclerView recyclerView;
    MenuAdapter menuAdapter;
    List<Menu> menuList;


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = view.findViewById(R.id.menuRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        menuList = new ArrayList<>();
        menuList.add(
                new Menu(
                        1,
                        "Cake",
                        R.drawable.cake,
                        false));
        menuList.add(
                new Menu(
                        1,
                        "Noodles",
                        R.drawable.noodles,
                        false));
        menuList.add(
                new Menu(
                        1,
                        "Onthon",
                        R.drawable.onthon,
                        false));

        menuAdapter = new MenuAdapter(getContext(), menuList);
        recyclerView.setAdapter(menuAdapter);

        return view;
    }

}
