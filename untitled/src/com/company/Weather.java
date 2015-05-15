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
    //private List<Integer> revertList = new ArrayList<Integer>();
    private ArrayList<ArrayList<Integer>> globalResult = new ArrayList<ArrayList<Integer>>();
    private Integer currentDelta = maxDelta;
    private Integer searchSize = 14;
    BufferedWriter outputWriter;


    public void find() throws IOException {
        initBufferedWriter();
        readAllDataFromFile();
     //   revertData();
        readDatesFromFile();
        for(int i = 0; i < allList.size() - searchSize; i++) {
            initSearchList(i);
            search();
        }
        closeBufferedWriter();
    }
/*
    private void revertData() {
        for(int i = allList.size() - 1; i >= 0; i--) {
            revertList.add(allList.get(i));
        }
    }
*/
    private void search() throws IOException {
        downSearch();
        if(globalResult.size() > 1) {


            outputWriter.newLine();
            outputWriter.write("   --- WEEK start ---   ");
            outputWriter.newLine();
            print();
            outputWriter.write("   --- WEEK end ---   ");
            outputWriter.newLine();
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

    private void readDatesFromFile() throws IOException {

        FileReader fileReader = new FileReader("dates.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            datesList.add(line);
        }
        bufferedReader.close();

    }


    private void readAllDataFromFile() throws IOException {
        String myDir = System.getProperty("user.dir");
        File file = new File( myDir + "/allDate.txt" );

        Scanner scanner = new Scanner(file);

        while(scanner.hasNextInt()){
            allList.add(scanner.nextInt());
        }
    }

    private void print() throws IOException {
        if(globalResult.size() == 1) {
            return;
        }
        for(ArrayList<Integer> resultList : globalResult) {
/*
            outputWriter.write(datesList.get(resultList.get(0)));
            outputWriter.newLine();
            outputWriter.write(datesList.get(resultList.get(searchSize - 1)));
            outputWriter.newLine();
            outputWriter.newLine();
*/

            for (Integer integer : resultList) {
                //outputWriter.write(Integer.toString(integer));
               // outputWriter.write("   ---   ");
                outputWriter.write(datesList.get(integer));
                outputWriter.newLine();
            }
            outputWriter.newLine();
            outputWriter.newLine();

        }
    }

    private void initBufferedWriter() throws IOException {
        String myDir = System.getProperty("user.dir");
        outputWriter = new BufferedWriter(new FileWriter(myDir + "/result.txt"));
    }

    private void closeBufferedWriter() throws IOException {
        outputWriter.flush();
        outputWriter.close();
    }

}


/*
        for (Integer integer : week) {
            System.out.println(integer);
        }
*/
