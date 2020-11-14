package sy.iyad.sybox.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import sy.iyad.mikrotik.MikrotikServer;
import sy.iyad.mikrotik.Models.ExecutionEventListener;
import sy.iyad.sybox.R;
import sy.iyad.sybox.Utils.Users;
import sy.iyad.sybox.Utils.UsersAdapter;
import sy.iyad.sybox.Utils.UsersIntRandom;

import static sy.iyad.sybox.ServerInformations.USER_PROFILES;


public class Generator extends Fragment {

    private OnFragmentInteractionListener mListener;

    UsersAdapter adapter;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList;
    ArrayList<Users> usersArrayList;

    Button button;
    EditText adet;
    EditText plength;
    EditText prefix;
    RecyclerView recyclerView;
    Spinner spinner;
    TextView textView;
    EditText ulength;

    public Generator() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button = view.findViewById(R.id.genbtn);
        adet = view.findViewById(R.id.adet);
        ulength = view.findViewById(R.id.lengthu);
        plength = view.findViewById(R.id.lengthp);
        prefix = view.findViewById(R.id.prefix);
        spinner = view.findViewById(R.id.spinner);
        textView = view.findViewById(R.id.wa);
        recyclerView = view.findViewById(R.id.listax);

        init();

        loadProfiles();

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                try {

                    int ad = Integer.parseInt(adet.getText().toString());
                    int pass = Integer.parseInt(plength.getText().toString());

                    createUsers(ad, Integer.parseInt(ulength.getText().toString()), prefix.getText().toString(), pass);
                } catch (NumberFormatException e) {

                    textView.setText(e.getMessage());
                }
            }
        });
    }

        private void init(){

            arrayList = new ArrayList<>();
            usersArrayList = new ArrayList<>();
            adapter = new UsersAdapter(usersArrayList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            arrayAdapter  = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arrayList);
            spinner.setAdapter(arrayAdapter);
        }

        private void loadProfiles() {

            MikrotikServer.execute(USER_PROFILES).addExecutionEventListener(new ExecutionEventListener() {

                public void onExecutionSuccess(List<Map<String, String>> mapList) {

                    for (Map<String, String> map : mapList) {

                        arrayList.add(map.get("name"));
                    }
                    arrayAdapter.notifyDataSetChanged();
                }

                public void onExecutionFailed(Exception exp) {
                    textView.setText(exp.getMessage());
                }
            });
        }

        private void createUsers(int adet, int userLength, String prefix, int passwordLenth) {


            for (int j = 0; j < adet; j++) {

                String username = prefix + new UsersIntRandom().ditectRand(userLength);
                int password = new UsersIntRandom().ditectRand(passwordLenth);
                String createUser = "/tool/user-manager/user/add customer=admin username="+ username+ " password="+ password;

                MikrotikServer.execute(createUser);

                String setupProfile= "/tool/user-manager/user/create-and-activate-profile customer=admin numbers="+ username+ " profile="+ spinner.getSelectedItem().toString();

                usersArrayList.add(new Users(username, password, spinner.getSelectedItem().toString()));

                MikrotikServer.execute(setupProfile).addExecutionEventListener(new ExecutionEventListener() {
                    @SuppressLint("SetTextI18n")
                    public void onExecutionSuccess(List<Map<String, String>> list) {

                        textView.setText(+adapter.getItemCount()+"تم بنجاح عدد المستخدمين المضافين");
                        adapter.notifyDataSetChanged();

                    }

                    public void onExecutionFailed(Exception exp) {
                        textView.setText(exp.getMessage());
                    }
                });
            }
            adapter.notifyDataSetChanged();
        }


    public static Generator newInstance(String param1, String param2) {
        return new Generator();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_generate, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
