package com.artitk.benchmarkcode.fragment;

import android.os.Bundle;

public class IfSwitchFragment extends BaseFragment {

    public static IfSwitchFragment newInstance() {
        IfSwitchFragment fragment = new IfSwitchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, 2);
        args.putStringArray(ARG_HEADERS, new String[] {"If-Else", "Switch-Case"});
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void setRandomRange() {
        super.randomMin = 0;
        super.randomMax = 4;
    }

    @Override
    protected void runCase(int bmCase, byte bmInput) {
        String result;

        switch (bmCase) {
            case 1: // If-Else
                if (bmInput == 4) {
                    result = "A";
                } else if (bmInput == 3) {
                    result = "B";
                } else if (bmInput == 2) {
                    result = "C";
                } else if (bmInput == 1) {
                    result = "D";
                } else {
                    result = "F";
                }
                break;
            case 2: // Switch-Case
                switch (bmInput) {
                    case 4:
                        result = "A";
                        break;
                    case 3:
                        result = "B";
                        break;
                    case 2:
                        result = "C";
                        break;
                    case 1:
                        result = "D";
                        break;
                    default:
                        result = "F";
                }
                break;
        }
    }
}
