package com.artitk.benchmarkcode.fragment;

import android.os.Bundle;

public class LoopRecursiveFragment extends BaseFragment {

    public static LoopRecursiveFragment newInstance() {
        LoopRecursiveFragment fragment = new LoopRecursiveFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, 4);
        args.putStringArray(ARG_HEADERS, new String[] {"Do-While", "Recursive"});
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void runCase(int bmCase, byte bmInput) {
        int result = 0;

        switch (bmCase) {
            case 1: // Do-While
                int i = 0;
                do {
                    result += i;
                    i++;
                } while (i < bmInput);
                break;
            case 2: // Recursive
                result = recursiveCase(0, bmInput);
                break;
        }
    }

    private int recursiveCase(int i, byte bmInput) {
        int result = 0;

        if (i < bmInput) {
            result = i + recursiveCase(i + 1, bmInput);
        }

        return result;
    }
}
