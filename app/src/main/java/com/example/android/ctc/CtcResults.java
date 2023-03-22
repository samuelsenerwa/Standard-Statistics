package com.example.android.ctc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.android.ctc.databinding.FragmentSecondBinding;


public class CtcResults extends Fragment {

    private FragmentSecondBinding binding;

    private static CtcResults instance;

    private MainActivity activitymain;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        instance = this;

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activitymain = (MainActivity) getActivity();

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CtcResults.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//                activitymain.
            }
        });


        String three_ms_voice_activated = activitymain.three_ms_voice_activated;
        String calculated_as = activitymain.calculated_as;
        String calculation_type = activitymain.calculation_type;
        setValues(three_ms_voice_activated, calculated_as, calculation_type);

        binding.savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activitymain.savepdf();
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void toFirst() {
        NavHostFragment.findNavController(CtcResults.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment);
    }

    public void setValues(String three_ms_voice_activated, String calculated_as, String calculation_type) {
        binding.calculationStringType.setText(calculation_type);
//        binding.resultsareas.setText(three_ms_voice_activated);
        binding.calculatedas.setText(calculated_as);
    }

    public static CtcResults GetInstance() {
        return instance;
    }


}