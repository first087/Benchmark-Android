package com.artitk.benchmarkcode.fragment;

import android.os.Bundle;

public class IfFragment extends BaseFragment {

    public static IfFragment newInstance() {
        IfFragment fragment = new IfFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, 1);
        args.putStringArray(ARG_HEADERS, new String[] {"If-Else", "Default-If", "Ternary Operator"});
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void runCase(int bmCase, byte bmInput) {
        boolean result;

        switch (bmCase) {
            case 1: // If-Else
                if (bmInput >= 51) {
                    result = true;
                } else {
                    result = false;
                }
                break;
            case 2: // Default-If
                result = false;
                if (bmInput >= 51) result = true;
                break;
            case 3: // Ternary operator
                result = bmInput >= 51 ? true : false;
                break;
        }
    }
}
