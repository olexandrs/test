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
    private Integer searchSize = 6;

    private TextFile file = new TextFile();
    private Table table = new Table();

    private List<String> datesList = new ArrayList<String>();
    private List<Integer> allList = new ArrayList<Integer>();
    private ArrayList<Integer> searchList = new ArrayList<Integer>();
    private ArrayList<Integer> searchListNumber = new ArrayList<Integer>();
    private ArrayList<Integer> upSearchList = new ArrayList<Integer>();
    private ArrayList<Integer> upSearchListNumber = new ArrayList<Integer>();
    private ArrayList<ArrayList<Integer>> globalResult = new ArrayList<ArrayList<Integer>>();
    private ArrayList<ArrayList<Integer>> upGlobalResult = new ArrayList<ArrayList<Integer>>();
    private ArrayList<ArrayList<Integer>> globalResultValue = new ArrayList<ArrayList<Integer>>();
    private ArrayList<ArrayList<Integer>> delta = new ArrayList<ArrayList<Integer>>();
    private ArrayList<Integer> emptyDelta;
    private ArrayList<ArrayList<Integer>> upGlobalResultValue = new ArrayList<ArrayList<Integer>>();
    private ArrayList<Integer> startDays = new ArrayList<Integer>();
    private ArrayList<Integer> endDays = new ArrayList<Integer>();

    private Integer startYear = 2004;
    private Integer endYear = 2004;


    private Integer startSearch;
    private Integer endSearch;
    private Integer startList;
    private Integer endList;

    private boolean shouldSave = false;


    private void initStartAndEndDate() {
        startDays.add(0);
        startDays.add(732);
        startDays.add(1462);
        startDays.add(2192);
        startDays.add(2922);
        startDays.add(3654);
        startDays.add(4384);
        startDays.add(5114);
        startDays.add(5844);
        startDays.add(6576);
        startDays.add(7306);
        startDays.add(8036);

        endDays.add(770);
        endDays.add(1500);
        endDays.add(2230);
        endDays.add(2960);
        endDays.add(3692);
        endDays.add(4422);
        endDays.add(5151);
        endDays.add(5881);
        endDays.add(6613);
        endDays.add(7343);
        endDays.add(8073);
        endDays.add(8334);
    }


    public void find() throws IOException {

        initStartAndEndDate();
        prepareEmptyDelta();
        file.readAllDataFromFile(allList);
        file.readDatesFromFile(datesList);

        for(int first = 0; first < 12; first++) {
            for(int second = 0 + first; second < 12; second++) {
                shouldSave = false;
                table = new Table();
                startSearch = startDays.get(first);
                endSearch = endDays.get(first);
                startList = startDays.get(second);
                endList = endDays.get(second);
                for (int i = startSearch; i < endSearch - searchSize; i++) {
                    initSearchList(i);
                    search();
                }
                if(shouldSave) {
                    table.save((startYear + first) + " - " + (endYear + second) + ".xlsx");
                }
            }
        }
    //    file.closeBufferedWriter();
    }

    private void search() throws IOException {
        downSearch();
        if(globalResult.size() > 1) {
            table.fillTable(prepareResult(globalResult), globalResultValue, delta, searchSize);
            shouldSave = true;
        }
        upSearch();
        if(upGlobalResult.size() > 1) {
            table.fillTable(prepareResult(upGlobalResult), upGlobalResultValue, delta, searchSize);
            shouldSave = true;
        }
    }

    private ArrayList<ArrayList<String>> prepareResult(ArrayList<ArrayList<Integer>> globalResult) {
        ArrayList<ArrayList<String>> resultAll = new ArrayList<ArrayList<String>>();
        ArrayList<String> resultOne;
        for(ArrayList<Integer> resultList : globalResult) {
            resultOne = new ArrayList<String>();
            for (Integer integer : resultList) {
                resultOne.add(datesList.get(integer));
            }
            resultAll.add(resultOne);
        }
        return resultAll;
    }

    private void upSearch() {
        upGlobalResult = new ArrayList<ArrayList<Integer>>();
        upGlobalResultValue = new ArrayList<ArrayList<Integer>>();
        delta = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> resultList = new ArrayList<Integer>();
        ArrayList<Integer> values = new ArrayList<Integer>();
        ArrayList<Integer> delt = new ArrayList<Integer>();
        upGlobalResult.add(upSearchListNumber);
        upGlobalResultValue.add(upSearchList);
        delta.add(emptyDelta);
        for (int i = startList; i < endList; i++) {
            for (int j = 0; j < upSearchList.size(); j++) {
                if (i + j > endList - 1) {
                    break;
                }
                Integer fromAll = allList.get(i + j);
                Integer fromSearch = upSearchList.get(j);
                Integer newDelta = fromAll - fromSearch;
                if (currentDelta == maxDelta) {
                    currentDelta = newDelta;
                    delt.add(newDelta);
                    values.add(allList.get(i + j));
                    resultList.add(i + j);
                    continue;
                }
                if (newDelta - shift <= currentDelta && currentDelta <= newDelta + shift) {
                    resultList.add(i + j);
                    values.add(allList.get(i + j));
                    delt.add(newDelta);
                } else {
                    resultList.clear();
                    values.clear();
                    delt.clear();

                    currentDelta = maxDelta;
                    break;
                }
                if (j >= upSearchList.size() - 1) {
                    if(needToAdd(values, upSearchList)) {
                        upGlobalResult.add(resultList);
                        upGlobalResultValue.add(values);
                        delta.add(delt);
                    }
                    resultList = new ArrayList<Integer>();
                    values = new ArrayList<Integer>();
                    delt = new ArrayList<Integer>();
                    currentDelta = maxDelta;
                    break;
                }
            }
        }
        if(upGlobalResult.size() == 1) {
            upGlobalResult = new ArrayList<ArrayList<Integer>>();
            upGlobalResultValue = new ArrayList<ArrayList<Integer>>();
            delta = new ArrayList<ArrayList<Integer>>();
        }
    }

    private void downSearch() {
        globalResult = new ArrayList<ArrayList<Integer>>();
        globalResultValue = new ArrayList<ArrayList<Integer>>();
        delta = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> resultList = new ArrayList<Integer>();
        ArrayList<Integer> values = new ArrayList<Integer>();
        ArrayList<Integer> delt = new ArrayList<Integer>();
        globalResult.add(searchListNumber);
        globalResultValue.add(searchList);
        delta.add(emptyDelta);
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
                    delt.add(newDelta);
                    continue;
                }
                if (newDelta - shift <= currentDelta && currentDelta <= newDelta + shift) {
                    resultList.add(i + j);
                    values.add(allList.get(i + j));
                    delt.add(newDelta);
                } else {
                    resultList.clear();
                    values.clear();
                    delt.clear();
                    currentDelta = maxDelta;
                    break;
                }
                if (j >= searchList.size() - 1) {
                    if(needToAdd(values, searchList)) {
                        globalResult.add(resultList);
                        globalResultValue.add(values);
                        delta.add(delt);
                    }
                    resultList = new ArrayList<Integer>();
                    values = new ArrayList<Integer>();
                    delt = new ArrayList<Integer>();
                    currentDelta = maxDelta;
                    break;
                }
            }
        }
        if(globalResult.size() == 1) {
            globalResult = new ArrayList<ArrayList<Integer>>();
            globalResultValue = new ArrayList<ArrayList<Integer>>();
            delta = new ArrayList<ArrayList<Integer>>();
        }
    }

    private boolean needToAdd(ArrayList<Integer> results, ArrayList<Integer> searchList) {
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
        upSearchList = new ArrayList<Integer>();
        upSearchListNumber = new ArrayList<Integer>();
        for(int i = 0; i < searchSize; i++ ) {
            searchList.add(allList.get(start + i));
            searchListNumber.add(start + i);
        }
        for(int j = searchSize - 1; j >= 0; j--) {
            upSearchList.add(searchList.get(j));
            upSearchListNumber.add(searchListNumber.get(j));
        }
    }

    private void prepareEmptyDelta() {
        emptyDelta = new ArrayList<Integer>();
        for(int i = 0; i < 1; i++) {
            emptyDelta.add(0);
        }
    }
}
