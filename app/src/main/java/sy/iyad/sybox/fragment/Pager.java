package sy.iyad.sybox.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import sy.iyad.mikrotik.MikrotikServer;
import sy.iyad.mikrotik.Models.ExecutionEventListener;
import sy.iyad.sybox.R;

import static sy.iyad.sybox.ServerInformations.CPU_COMMAND;
import static sy.iyad.sybox.ServerInformations.RUNNING_TRUE;
import static sy.iyad.sybox.ServerInformations.UPTIME_COMMAND;
import static sy.iyad.sybox.ServerInformations.VOLTAGE_COMMAND;


public class Pager extends Fragment {

    @SuppressLint("StaticFieldLeak")
    Button button1;
    Button button2;
    Button logger;
    FloatingActionButton actionButton;

    TextView cput;
    TextView uptimet;
    TextView voltaget;
    RadioButton ether1;
    RadioButton ether10;
    RadioButton ether11;
    RadioButton ether12;
    RadioButton ether13;
    RadioButton ether2;
    RadioButton ether3;
    RadioButton ether4;
    RadioButton ether5;
    RadioButton ether6;
    RadioButton ether7;
    RadioButton ether8;
    RadioButton ether9;

    Handler handler = new Handler(Looper.getMainLooper());

    private HomeFragment.OnFragmentInteractionListener mListener;

    public Pager() {

    }

    private static void checkarsin(RadioButton radioButton, boolean isChecked) {
        radioButton.setChecked(isChecked);
    }

    private void turnOnOffLeds() {

        MikrotikServer.execute(RUNNING_TRUE).addExecutionEventListener(new ExecutionEventListener() {
            public void onExecutionSuccess(List<Map<String, String>> mapList) {
                Iterator<Map<String, String>> it = mapList.iterator();
                while (it.hasNext()) {

                    Map<String, String> map = it.next();
                    boolean e1 = map.containsValue("IN");
                    boolean e2 = map.containsValue("LAN2");
                    boolean e3 = map.containsValue("LAN3");
                    boolean e4 = map.containsValue("LAN4");
                    boolean e5 = map.containsValue("LAN5");
                    boolean e6 = map.containsValue("LAN6");
                    boolean e7 = map.containsValue("LAN7");
                    boolean e8 = map.containsValue("LAN8");
                    boolean e9 = map.containsValue("LAN9");
                    boolean e10 = map.containsValue("LANa");
                    boolean e11 = map.containsValue("LANb");
                    boolean e12 = map.containsValue("LANc");
                    boolean e13 = map.containsValue("LANd");
                    Iterator<Map<String, String>> it2 = it;
                    checkarsin(ether1, e1);
                    checkarsin(ether2, e2);
                    checkarsin(ether3, e3);
                    checkarsin(ether4, e4);
                    checkarsin(ether5, e5);
                    checkarsin(ether6, e6);
                    checkarsin(ether7, e7);
                    checkarsin(ether8, e8);
                    checkarsin(ether9, e9);
                    checkarsin(ether10, e10);
                    checkarsin(ether11, e11);
                    checkarsin(ether12, e12);
                    checkarsin(ether13, e13);
                    it = it2;
                }
            }

            public void onExecutionFailed(Exception exp) {
                voltaget.setText(exp.getMessage());
            }
        });
    }

    protected void getVoltage() {

        MikrotikServer.execute(VOLTAGE_COMMAND).addExecutionEventListener(new ExecutionEventListener() {
            public void onExecutionSuccess(List<Map<String, String>> mapList) {
                voltaget.setText(mapList.get(0).get("voltage"));

            }

            public void onExecutionFailed(Exception exp) {
                voltaget.setText(exp.getMessage());
            }
        });
    }


    private void loadCpu() {
        ReUpdateNow reUpdateNow = new ReUpdateNow(cput, CPU_COMMAND, "load");
        Thread thread = new Thread(reUpdateNow);
        thread.setDaemon(true);
        thread.start();
    }

    private void loadUptime() {
        ReUpdateNow reUpdateNow = new ReUpdateNow(uptimet, UPTIME_COMMAND, "uptime");
        Thread thread = new Thread(reUpdateNow);
        thread.setDaemon(true);
        thread.start();
    }

    private void loadTask() {

        TaskLeds taskLeds = new TaskLeds();
        Thread thread = new Thread(taskLeds);
        thread.setDaemon(true);
        thread.start();
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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


        return inflater.inflate(R.layout.fragment_pager, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button1 = view.findViewById(R.id.commandact);
        button2 = view.findViewById(R.id.usergenact);
        logger = view.findViewById(R.id.logger);
        cput = view.findViewById(R.id.cpu);
        voltaget = view.findViewById(R.id.voltage);
        uptimet = view.findViewById(R.id.uptime);
        ether1 = view.findViewById(R.id.radioButton);
        ether2 = view.findViewById(R.id.radioButton2);
        ether3 = view.findViewById(R.id.radioButton3);
        ether4 = view.findViewById(R.id.radioButton4);
        ether5 = view.findViewById(R.id.radioButton5);
        ether6 = view.findViewById(R.id.radioButton6);
        ether7 = view.findViewById(R.id.radioButton7);
        ether8 = view.findViewById(R.id.radioButton8);
        ether9 = view.findViewById(R.id.radioButton9);
        ether10 = view.findViewById(R.id.radioButton10);
        ether11 = view.findViewById(R.id.radioButton11);
        ether12 = view.findViewById(R.id.radioButton12);
        ether13 = view.findViewById(R.id.radioButton13);

        actionButton = view.findViewById(R.id.dis);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MikrotikServer.disconnect();
                getActivity().finish();
            }
        });
        logger.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                loadTask();
                loadCpu();
                loadUptime();
                getVoltage();
            }
        });

        loadCpu();
        loadTask();
        loadUptime();
        getVoltage();
    }


    private  class TaskLeds implements Runnable {

        public TaskLeds() {
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(1000);

                    handler.post(new Runnable() {

                        public void run() {
                            turnOnOffLeds();
                        }

                    });
                } catch (InterruptedException e) {
                    cput.setText(e.getMessage());
                }
            }
        }
    }

    private class ReUpdateNow implements Runnable {

        String cmdx;
        String keys;
        TextView textViewx;
        boolean isRun = true;

        public ReUpdateNow(@NonNull TextView textViewx, @NonNull String cmdx, @NonNull String keys) {

            this.textViewx = textViewx;
            this.cmdx = cmdx;
            this.keys = keys;

        }
        public void run() {

            try {
                while (!Thread.currentThread().isInterrupted()) {
                    //noinspection BusyWait
                    Thread.sleep(1000);
                    handler. post(new Runnable() {
                        public void run() {
                            MikrotikServer.execute(cmdx).addExecutionEventListener(new ExecutionEventListener() {
                                public void onExecutionSuccess(List<Map<String, String>> mapList) {

                                    textViewx.setText(mapList.get(0).get(keys));

                                }

                                public void onExecutionFailed(Exception exp) {
                                    textViewx.setText(exp.getMessage());
                                    isRun = false;
                                }
                            });
                        }
                    });
                }
            } catch (InterruptedException e) {
                textViewx.setText(e.getMessage());
                isRun = false;
            }
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
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
}
