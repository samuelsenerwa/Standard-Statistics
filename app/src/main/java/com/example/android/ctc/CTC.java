package com.example.android.ctc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.android.ctc.databinding.FragmentFirstBinding;

public class CTC extends Fragment {

    private FragmentFirstBinding binding;
    private static CTC instance;

    private MainActivity activitymain;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        instance = this;

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activitymain = (MainActivity)getActivity();
        String input_values = activitymain.input_values;

        if(input_values != ""){
            binding.inputValues.setText(input_values);
        }


    }


    public void toSecond(){
        NavHostFragment.findNavController(CTC.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment);
    }

    public static CTC GetInstance(){
        return instance;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}