package com.example.exercicio1topicos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CustomerListFragment extends Fragment {

    private Bundle bundle;
    private ArrayList<User> userList;
    private ListView lvUserList;
    private FloatingActionButton fabAdd;
    private ArrayAdapter<User> adapterUserList;

    public CustomerListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_list, container, false);

        initializeComponents(view);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new CustomerFormFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container_view, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        lvUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), UserDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("USER_OBJECT", userList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }

    private void initializeComponents(View view) {
        this.bundle = getActivity().getIntent().getExtras();
        if(bundle != null) {
            this.userList = (ArrayList<User>) bundle.getSerializable("USER_LIST");
            this.adapterUserList = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, userList);
            this.lvUserList.setAdapter(adapterUserList);
        }
        this.lvUserList = view.findViewById(android.R.id.list);
        this.fabAdd = view.findViewById(R.id.fab);
    }
}