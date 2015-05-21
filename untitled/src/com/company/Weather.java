package com.company;

import java.io.*;
import java.util.*;

/**
 * Created by aleksandr on 01.05.15.
 */
public class Weather {

    private final Integer maxDelta = 1000;
    private final Integer shift = 2;

    private Integer currentDelta = maxDelta;
    private Integer searchSize = 14;

    private TextFile file = new TextFile();

    private List<String> datesList = new ArrayList<String>();
    private List<Integer> allList = new ArrayList<Integer>();
    private ArrayList<Integer> searchList = new ArrayList<Integer>();
    private ArrayList<Integer> searchListNumber = new ArrayList<Integer>();
    private ArrayList<ArrayList<Integer>> globalResult = new ArrayList<ArrayList<Integer>>();


    private Integer startSearch = 0;
    private Integer endSearch = 669;
//    private Integer startSearch = 683;
//    private Integer endSearch = 1413;
//    private Integer startList = 669;
//    private Integer endList = 1399;
    private Integer startList = 7974;
    private Integer endList = 8243;


    public void find() throws IOException {

        file.initBufferedWriter();

        file.readAllDataFromFile(allList);
        file.readDatesFromFile(datesList);

        for(int i = startSearch; i < endSearch - searchSize; i++) {
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

    private void downSearch() {
        globalResult = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> resultList = new ArrayList<Integer>();
        ArrayList<Integer> values = new ArrayList<Integer>();
        globalResult.add(searchListNumber);
        for (int i = startList; i < endList; i++) {
            for (int j = 0; j < searchList.size(); j++) {
                if (i + j > endList - 1) {
                    break;
                }
                Integer fromAll = allList.get(i + j);
                Integer fromSearch = searchList.get(j);
                Integer newDelta = fromAll - fromSearch;
                if (currentDelta == maxDelta) {
                    currentDelta = newDelta;
                    values.add(allList.get(i + j));
                    resultList.add(i + j);
                    continue;
                }
                if (newDelta - shift <= currentDelta && currentDelta <= newDelta + shift) {
                    resultList.add(i + j);
                    values.add(allList.get(i + j));
                } else {
                    resultList.clear();
                    values.clear();
                    currentDelta = maxDelta;
                    break;
                }
                if (j >= searchList.size() - 1) {
                    if(needToAdd(values)) {
                        globalResult.add(resultList);
                    }
                    resultList = new ArrayList<Integer>();
                    values = new ArrayList<Integer>();
                    currentDelta = maxDelta;
                    break;
                }
            }
        }
        if(globalResult.size() == 1) {
            globalResult = new ArrayList<ArrayList<Integer>>();
        }
    }

    private boolean needToAdd(ArrayList<Integer> results) {
        for(int i = 0; i < results.size(); i++) {
            if(results.get(i) != searchList.get(i)) {
                return true;
            }
        }
        return false;
    }

    private void initSearchList(int start) {
        searchList = new ArrayList<Integer>();
        searchListNumber = new ArrayList<Integer>();
        for(int i = 0; i < searchSize; i++ ) {
            searchList.add(allList.get(start + i));
            searchListNumber.add(start + i);
        }
    }
}
