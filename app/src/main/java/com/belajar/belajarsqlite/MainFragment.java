package com.belajar.belajarsqlite;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener,RecyclerViewAdaper.OnUserClickListener {
    RecyclerView recyclerView;
    EditText edtName,edtAge;
    Button btnSubmit;
    Context context;
    RecyclerView.LayoutManager layoutManager;
    List<PersonBean> listPersonInfo;
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         context = getActivity();
         recyclerView = view.findViewById(R.id.recyclerview);
         layoutManager = new LinearLayoutManager(context);
         edtName = view.findViewById(R.id.edtname);
         edtAge = view.findViewById(R.id.edtage);
         btnSubmit = view.findViewById(R.id.btnsubmit);
         btnSubmit.setOnClickListener(this);
         setupRecyclerView();
    }

    private void setupRecyclerView() {
        DataHelper db = new DataHelper(context);
        listPersonInfo = db.selectUserData();
        RecyclerViewAdaper adaper = new RecyclerViewAdaper(context,listPersonInfo,this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adaper);
        adaper.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnsubmit) {
            DataHelper db = new DataHelper(context);
            PersonBean currentPerson = new PersonBean();
            String btnStatus = btnSubmit.getText().toString();
            if (btnStatus.equals("Submit")) {
                currentPerson.setName(edtName.getText().toString());
                currentPerson.setAge(Integer.parseInt(edtAge.getText().toString()));
                db.insert(currentPerson);
            }
            if (btnStatus.equals("Update")) {
                currentPerson.setName(edtName.getText().toString());
                currentPerson.setAge(Integer.parseInt(edtAge.getText().toString()));
                db.update(currentPerson);
            }
            setupRecyclerView();
            edtName.setText("");
            edtAge.setText("");
            edtName.setEnabled(true);
            edtName.setFocusable(true);
            btnSubmit.setText("Submit");
        }
    }

    @Override
    public void onUserClick(PersonBean currentPerson, String action) {
        if (action.equals("Edit")){
            edtName.setText(currentPerson.getName());
            edtName.setFocusable(false);
            edtName.setEnabled(false);
            edtAge.setText(currentPerson.getAge()+"");
            btnSubmit.setText("Update");
        }
        if (action.equals("Delete")){
            DataHelper db = new DataHelper(context);
            db.delete(currentPerson.getName());
            setupRecyclerView();
        }
    }
}
