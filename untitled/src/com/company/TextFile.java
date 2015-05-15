package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Olexandr_Sulima on 5/15/2015.
 */
public class TextFile {

    BufferedWriter outputWriter;

    public void readDatesFromFile(List<String> datesList) throws IOException {

        FileReader fileReader = new FileReader("dates.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            datesList.add(line);
        }
        bufferedReader.close();

    }


    public void readAllDataFromFile(List<Integer> allList) throws IOException {
        String myDir = System.getProperty("user.dir");
        File file = new File( myDir + "/allDate.txt" );

        Scanner scanner = new Scanner(file);

        while(scanner.hasNextInt()){
            allList.add(scanner.nextInt());
        }
    }

    public void print(List<String> datesList, ArrayList<ArrayList<Integer>> globalResult) throws IOException {
        if(globalResult.size() == 1) {
            return;
        }
        outputWriter.newLine();
        outputWriter.write("   --- WEEK start ---   ");
        outputWriter.newLine();
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

        outputWriter.write("   --- WEEK end ---   ");
        outputWriter.newLine();
    }

    public void initBufferedWriter() throws IOException {
        String myDir = System.getProperty("user.dir");
        outputWriter = new BufferedWriter(new FileWriter(myDir + "/result.txt"));
    }

    public void closeBufferedWriter() throws IOException {
        outputWriter.flush();
        outputWriter.close();
    }
}
