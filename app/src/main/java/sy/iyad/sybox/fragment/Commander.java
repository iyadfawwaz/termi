package sy.iyad.sybox.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
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
import sy.iyad.sybox.Utils.ServerAdapter;

import static sy.iyad.sybox.ServerInformations.SERVERKEYWORDS;


public class Commander extends Fragment {


    ServerAdapter adapter;
    ArrayList<String> arrayList;
    ArrayAdapter<String> stringArrayAdapter;


    Button button;
    MultiAutoCompleteTextView comm;
    EditText keyg;
    RecyclerView recyclerView;
    TextView textView;

    private void init() {

        arrayList = new ArrayList<>();
        adapter = new ServerAdapter(arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        stringArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, SERVERKEYWORDS);
        comm.setAdapter(stringArrayAdapter);
        comm.setTokenizer(new SlashTokenizer());
        comm.setThreshold(1);
        comm.setText("/");
        stringArrayAdapter.notifyDataSetChanged();

    }


    private void sendComm(String command, final String key) {

        arrayList.clear();

        init();

        MikrotikServer.execute(command).addExecutionEventListener(new ExecutionEventListener() {
            @SuppressLint("SetTextI18n")
            public void onExecutionSuccess(@NonNull List<Map<String, String>> mapList) {

                if (key.equals("")) {

                    arrayList.add(mapList.toString());
                    System.out.println(mapList.toString());
                    adapter.notifyDataSetChanged();

                } else {

                    for (Map<String, String> map : mapList) {

                        if (!map.get(key).isEmpty()) {
                            arrayList.add(map.get(key));
                            System.out.println(map.get(key));
                        } else {
                            textView.setText("empty");
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                textView.setText("" + adapter.getItemCount());
            }

            public void onExecutionFailed(@NonNull Exception exception) {
                textView.setText(exception.getMessage());
            }
        });
    }

    private OnFragmentInteractionListener mListener;

    public Commander() {

    }

    public static Commander newInstance(String param1, String param2) {
        Commander fragment = new Commander();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_commander, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview);
        textView = view.findViewById(R.id.warn);
        comm = view.findViewById(R.id.command);
        keyg = view.findViewById(R.id.key);
        button = view.findViewById(R.id.sendcomm);
        init();

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                String ke = keyg.getText().toString();
                String co = comm.getText().toString();

                sendComm(co, ke);
            }
        });
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


    static class SlashTokenizer implements MultiAutoCompleteTextView.Tokenizer {

        @Override
        public int findTokenStart(CharSequence text, int cursor) {

            int i = cursor;
            while (i > 0 && text.charAt(i - 1) != '/') {
                i--;
            }

            while (i < cursor && text.charAt(i) == '/') {
                i++;
            }
            return i;
        }

        @Override
        public int findTokenEnd(CharSequence text, int cursor) {

            int i = cursor;
            int len = text.length();
            while (i < len) {
                if (text.charAt(i) == '/') {
                    return i;
                } else {
                    i++;
                }
            }

            return len;
        }

        @Override
        public CharSequence terminateToken(CharSequence text) {

            int i = text.length();
            while (i > 0 && text.charAt(i - 1) == '/') {
                i--;
            }
            if (i > 0 && text.charAt(i - 1) == '/') {
                return text;
            } else {
                if (text instanceof Spanned) {
                    SpannableString string = new SpannableString(text + "/");
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, string, 0);
                    return string;

                } else {
                    if (text == "print") {
                        return text;
                    } else {

                    }
                    return text + "/";
                }
            }
        }
    }
}
