package com.artitk.benchmarkcode.fragment;

import android.os.Bundle;

public class LoopFragment extends BaseFragment {

    public static LoopFragment newInstance() {
        LoopFragment fragment = new LoopFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, 3);
        args.putStringArray(ARG_HEADERS, new String[] {"For", "While", "Do-While"});
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void runCase(int bmCase, byte bmInput) {
        int result = 0;

        switch (bmCase) {
            case 1: // For
                for (byte i = 0; i < bmInput; i++) {
                    result += i;
                }
                break;
            case 2: // While
                byte j = 0;
                while (j < bmInput) {
                    result += j;
                    j++;
                }
                break;
            case 3: // Do-While
                byte k = 0;
                do {
                    result += k;
                    k++;
                } while (k < bmInput);
                break;
        }
    }
}
