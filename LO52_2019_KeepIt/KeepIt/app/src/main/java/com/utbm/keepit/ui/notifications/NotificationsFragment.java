package com.utbm.keepit.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.utbm.keepit.MyApp;
import com.utbm.keepit.R;
import com.utbm.keepit.activities.ChangePwdActivity;
import com.utbm.keepit.activities.LoginActivity;
import com.utbm.keepit.activities.SeanceListActivity;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private TextView userName;
    private Button logoutBtn;
    private TextView changePwd ,afficherSeance;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        notificationsViewModel =
//                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        userName = root.findViewById(R.id.userName);

        userName.setText(MyApp.getUser().getName());

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        logoutBtn = (Button) getActivity().findViewById(R.id.btn_logout);
        changePwd = (TextView) getActivity().findViewById(R.id.change_pwd);
        afficherSeance=(TextView) getActivity().findViewById(R.id.afficher_seance);
        logoutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MyApp.setUser(null);
                Intent intent= new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        changePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), ChangePwdActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        afficherSeance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), SeanceListActivity.class);
                startActivity(intent);
            }
        });
    }

    //onLogoutClick  onChangePwdClick
//    public void onChangePwdClick(View view) {
//        Intent intent= new Intent(this.getActivity(), ChangePwdActivity.class);
//        startActivity(intent);
//        this.getActivity().finish();
//    }
//
//    public void onLogoutClick(View view){
////        UserUtils.logout(this);
//        Intent intent= new Intent(this.getActivity(), LoginActivity.class);
//        startActivity(intent);
//        this.getActivity().finish();
//

}