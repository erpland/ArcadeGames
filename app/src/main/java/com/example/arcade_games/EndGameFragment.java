package com.example.arcade_games;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EndGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EndGameFragment extends Fragment implements View.OnClickListener {
    Button tryAgainBtn, goBackBtn;
    TextView headlineTv, subTextTv;
    ViewGroup root;
    CallBackInterface callBackInterface;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public EndGameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EndGameFragment.
     */

    public static EndGameFragment newInstance(String param1, String param2) {
        EndGameFragment fragment = new EndGameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callBackInterface = (CallBackInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_end_game, null);
        initViews();
        Bundle bundle = this.getArguments();
        headlineTv.setText(bundle.getString("headline"));
        subTextTv.setText(bundle.getString("subText"));

        tryAgainBtn.setOnClickListener(this);
        goBackBtn.setOnClickListener(this);
        return root;
    }

    private void initViews() {
        tryAgainBtn = root.findViewById(R.id.btn_tryAgain);
        goBackBtn = root.findViewById(R.id.btn_goBack);
        headlineTv = root.findViewById(R.id.tv_headline);
        subTextTv = root.findViewById(R.id.tv_subText);

    }

    @Override
    public void onClick(View view) {
        callBackInterface.callBack(view.getId());
    }
}