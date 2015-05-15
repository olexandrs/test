package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by aleksandr on 01.05.15.
 */
public class Weather {

    private final Integer maxDelta = 1000;
    private final Integer shift = 2;

    private List<String> datesList = new ArrayList<String>();
    private List<Integer> searchList = new ArrayList<Integer>();
    private List<Integer> allList = new ArrayList<Integer>();
    private ArrayList<ArrayList<Integer>> globalResult = new ArrayList<ArrayList<Integer>>();
    private Integer currentDelta = maxDelta;
    private Integer searchSize = 14;
    private TextFile file = new TextFile();


    public void find() throws IOException {

        file.initBufferedWriter();
        file.readAllDataFromFile(allList);
        file.readDatesFromFile(datesList);

        for(int i = 0; i < allList.size() - searchSize; i++) {
            initSearchList(i);
            search();
        }

        file.closeBufferedWriter();
    }

    private void search() throws IOException {
        downSearch();
        if(globalResult.size() > 1) {
            file.print(datesList, globalResult);
        }
    }

    private void initSearchList(int start) {
        searchList = new ArrayList<Integer>();
        for(int i = 0; i < searchSize; i++ ) {
            searchList.add(allList.get(start + i));
        }
    }

    private void downSearch() {
        globalResult = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> resultList = new ArrayList<Integer>();
        for (int i = 0; i < allList.size(); i++) {
            for (int j = 0; j < searchList.size(); j++) {
                if (i + j > allList.size() - 1) {
                    break;
                }
                Integer fromAll = allList.get(i + j);
                Integer fromSearch = searchList.get(j);
                Integer newDelta = fromAll - fromSearch;
                if (currentDelta == maxDelta) {
                    currentDelta = newDelta;
                    resultList.add(i + j);
                    continue;
                }
                if (newDelta - shift <= currentDelta && currentDelta <= newDelta + shift) {
                    resultList.add(i + j);
                } else {
                    resultList.clear();
                    currentDelta = maxDelta;
                    break;
                }
                if (j >= searchList.size() - 1) {
                    globalResult.add(resultList);
                    resultList = new ArrayList<Integer>();
                    currentDelta = maxDelta;
                    break;
                }
            }
        }
    }
}
