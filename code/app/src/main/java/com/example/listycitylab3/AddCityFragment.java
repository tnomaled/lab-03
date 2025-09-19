package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {

    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(City city, int index); // for edit
    }

    private AddCityDialogListener listener;
    private City cityToEdit = null; // initialize selected city for edit to null
    private int indexOfEdit = -1; // initialize position

    public static AddCityFragment newInstance(City city, int index) {
        Bundle args = new Bundle();
        args.putSerializable("city", city);
        args.putInt("city_index", index);
        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        }
        else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityText = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceText = view.findViewById(R.id.edit_text_province_text);

        if (getArguments() != null && getArguments().containsKey("city")) {
            cityToEdit = (City) getArguments().getSerializable("city");
            indexOfEdit = getArguments().getInt("city_index");
            editCityText.setText(cityToEdit.getName());
            editProvinceText.setText(cityToEdit.getProvince());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(cityToEdit == null ? "Add City" : "Edit City")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(cityToEdit == null ? "Add" : "Save", (dialog, which) -> {
                    String name = editCityText.getText().toString();
                    String province = editProvinceText.getText().toString();

                    if (cityToEdit == null) {
                        listener.addCity(new City(name, province));
                    }
                    else {
                        cityToEdit.setName(name);
                        cityToEdit.setProvince(province);
                        listener.editCity(cityToEdit, indexOfEdit);
                    }
                })
                .create();
    }
}
