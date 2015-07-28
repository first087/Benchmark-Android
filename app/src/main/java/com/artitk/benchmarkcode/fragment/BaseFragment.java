package com.artitk.benchmarkcode.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.artitk.benchmarkcode.MainActivity;
import com.artitk.benchmarkcode.R;
import com.artitk.benchmarkcode.data.SumResult;

import java.util.ArrayList;
import java.util.Random;

public abstract class BaseFragment extends Fragment {
    protected static final String ARG_SECTION_NUMBER = "section_number";
    protected static final String ARG_HEADERS = "headers";

    private static final byte MAX_ROUND = 10;   // DO NOT CHANGE
    private static final Integer MAX_LOOP_PER_ROUND = 10000000;

    private final int RANDOM_MIN = 1;
    private final int RANDOM_MAX = 100;

    private String[] headers;
    private ArrayList<Byte> randomList;

    public BaseFragment() {
        // Required empty public constructor
    }

    private void getRandom() {
        randomList = new ArrayList<>();

        Random rnd = new Random();

        for (int i=0; i<MAX_LOOP_PER_ROUND; i++) {
            try {
                randomList.add((byte) (rnd.nextInt(RANDOM_MAX - RANDOM_MIN + 1) + RANDOM_MIN));
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private long run(int bmCase) {
        long startTime = System.currentTimeMillis();

        for (byte item : randomList) {
            runCase(bmCase, item);
        }

        long endTime = System.currentTimeMillis();

        return endTime - startTime;
    }

    protected abstract void runCase(int bmCase, byte bmInput);

    private void clearRandom() {
        randomList.clear();
        randomList = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initInstances(view);

        return view;
    }

    private void initInstances(View view) {
        final ListView listView = (ListView) view.findViewById(R.id.listView);
        final Button buttonRun = (Button) view.findViewById(R.id.buttonRun);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        progressBar.setProgress(0);

        headers = getArguments().getStringArray(ARG_HEADERS);

        final ArrayList<SumResult> items = new ArrayList<>();
        final ListViewAdapter listViewAdapter = new ListViewAdapter(headers, items);
        listView.setAdapter(listViewAdapter);

        buttonRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.clear();
                listViewAdapter.notifyDataSetChanged();

                buttonRun.setEnabled(false);

                new AsyncTask<Integer, Message, long[]>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();

                        progressBar.setProgress(0);
                        progressBar.setIndeterminate(true);
                    }

                    @Override
                    protected long[] doInBackground(Integer... params) {
                        int totalCase = params[0];

                        getRandom();

                        Message message;

                        long[] sumResult = new long[totalCase];

                        for (int i=0; i<MAX_ROUND; i++) {
                            message = new Message();
                            message.what = 0;
                            publishProgress(message);

                            // TODO: Benchmark "Choice A"
                            long resultA = run(1);

                            message = new Message();
                            message.what = 1;
                            message.obj  = resultA;
                            sumResult[0] += resultA;
                            publishProgress(message);

                            if (totalCase >= 2) {
                                // TODO: Benchmark "Choice B"
                                long resultB = run(2);

                                message = new Message();
                                message.what = 2;
                                message.obj  = resultB;
                                sumResult[1] += resultB;
                                publishProgress(message);
                            }

                            if (totalCase >= 3) {
                                // TODO: Benchmark "Choice C"
                                long resultC = run(3);

                                message = new Message();
                                message.what = 3;
                                message.obj  = resultC;
                                sumResult[2] += resultC;
                                publishProgress(message);
                            }
                        }

                        return sumResult;
                    }

                    @Override
                    protected void onProgressUpdate(Message... values) {
                        super.onProgressUpdate(values);

                        if (progressBar.isIndeterminate()) progressBar.setIndeterminate(false);

                        Message message = values[0];
                        SumResult sumResult;

                        switch (message.what) {
                            case 0:
                                progressBar.setProgress(items.size() * 10);
                                sumResult = new SumResult(String.valueOf(items.size()+1));
                                items.add(sumResult);
                                break;
                            case 1:
                            case 2:
                            case 3:
                                progressBar.incrementProgressBy(3);
                                sumResult = items.get(items.size() - 1);
                                sumResult.getSumResult().add((long) message.obj);
                                break;
                        }

                        listViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    protected void onPostExecute(long[] longs) {
                        super.onPostExecute(longs);

                        clearRandom();

                        progressBar.setProgress(100);

                        SumResult sumResult = new SumResult("Sum");
                        sumResult.getSumResult().add(longs[0]);
                        if (longs.length >= 2) sumResult.getSumResult().add(longs[1]);
                        if (longs.length >= 3) sumResult.getSumResult().add(longs[2]);
                        items.add(sumResult);

                        SumResult avgResult = new SumResult("Avg");
                        avgResult.getSumResult().add(longs[0] / MAX_ROUND);
                        if (longs.length >= 2) avgResult.getSumResult().add(longs[1] / MAX_ROUND);
                        if (longs.length >= 3) avgResult.getSumResult().add(longs[2] / MAX_ROUND);
                        items.add(avgResult);

                        listViewAdapter.notifyDataSetChanged();

                        buttonRun.setEnabled(true);
                    }
                }.execute(headers.length);
            }
        });
    }

    private class ListViewAdapter extends BaseAdapter {

        private String[] headers;
        private ArrayList<SumResult> items;

        private ListViewAdapter(String[] headers, ArrayList<SumResult> items) {
            this.headers = headers;
            this.items   = items;
        }

        @Override
        public int getCount() {
            return items.size()+1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.view_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            initData(viewHolder, position);

            return convertView;
        }

        private void initData(ViewHolder viewHolder, int position) {
            viewHolder.textViewChoice2.setVisibility(headers.length >= 2 ? View.VISIBLE : View.GONE);
            viewHolder.textViewChoice3.setVisibility(headers.length >= 3 ? View.VISIBLE : View.GONE);

            int fontColor;
            switch (position) {
                case MAX_ROUND + 1: fontColor = android.R.color.holo_green_dark;    break;
                case MAX_ROUND + 2: fontColor = android.R.color.holo_blue_dark;     break;
                default:            fontColor = android.R.color.black;
            }

            viewHolder.textViewRound.setTextColor(getResources().getColor(fontColor));
            viewHolder.textViewChoice1.setTextColor(getResources().getColor(fontColor));
            if (headers.length >= 2) viewHolder.textViewChoice2.setTextColor(getResources().getColor(fontColor));
            if (headers.length >= 3) viewHolder.textViewChoice3.setTextColor(getResources().getColor(fontColor));

            switch (position) {
                case 0:     // Header
                    viewHolder.textViewRound.setText(R.string.round);
                    viewHolder.textViewChoice1.setText(headers[0]);
                    if (headers.length >= 2) viewHolder.textViewChoice2.setText(headers[1]);
                    if (headers.length >= 3) viewHolder.textViewChoice3.setText(headers[2]);
                    break;
                default:    // Data
                    viewHolder.textViewRound.setText(items.get(position - 1).getRound());

                    viewHolder.textViewChoice1.setText("-");
                    if (headers.length >= 2) viewHolder.textViewChoice2.setText("-");
                    if (headers.length >= 3) viewHolder.textViewChoice3.setText("-");

                    ArrayList<Long> sumResult = items.get(position - 1).getSumResult();
                    if (sumResult.size() >= 1) viewHolder.textViewChoice1.setText(sumResult.get(0).toString());
                    if (sumResult.size() >= 2) viewHolder.textViewChoice2.setText(sumResult.get(1).toString());
                    if (sumResult.size() >= 3) viewHolder.textViewChoice3.setText(sumResult.get(2).toString());
            }
        }

        private class ViewHolder {
            public TextView textViewRound;
            public TextView textViewChoice1;
            public TextView textViewChoice2;
            public TextView textViewChoice3;

            public ViewHolder(View convertView) {
                textViewRound   = (TextView) convertView.findViewById(R.id.textViewRound);
                textViewChoice1 = (TextView) convertView.findViewById(R.id.textViewChoice1);
                textViewChoice2 = (TextView) convertView.findViewById(R.id.textViewChoice2);
                textViewChoice3 = (TextView) convertView.findViewById(R.id.textViewChoice3);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
