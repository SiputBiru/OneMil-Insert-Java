package org.inserter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static final String JDBC_URL = "jdbc:postgresql://172.30.0.2:5432/OneMilInsert";
    private static final String USERNAME = "psql_user";
    private static final String PASSWORD = "password";

    private static final String CSV_FILE_PATH = "/home/siput/projects/java/OneMilInsertSQL/app/src/main/resources/spotify_data.csv";

    public static void main(String[] args) {
        final int NUM_THREADS = 8;

        System.out.println("Starting data loading from: " + CSV_FILE_PATH);

        List<Track> allTracks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String header = br.readLine();
            if (header == null) {
                System.err.println("File is empty!");
                return;
            }

            String line;
            while ((line = br.readLine()) != null) {
                Track track = Track.fromCsvLine(line);
                if (track != null) {
                    allTracks.add(track);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read the file: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        int TOTAL_ROWS = allTracks.size();
        System.out.println(TOTAL_ROWS + " tracks loaded from file.");

        if (TOTAL_ROWS == 0) {
            System.out.println("No data to insert. Exiting.");
            return;
        }

        List<Thread> threads = new ArrayList<>();
        int rowsPerThread = TOTAL_ROWS / NUM_THREADS;
        int remainingRows = TOTAL_ROWS % NUM_THREADS;

        System.out.println("Dividing " + TOTAL_ROWS + " rows among " + NUM_THREADS + " threads.");

        int startIdx = 0;
        for (int i = 0; i < NUM_THREADS; i++) {
            int numRowsForThisThread = rowsPerThread;
            if (i < remainingRows) {
                numRowsForThisThread++;
            }

            int endIdx = startIdx + numRowsForThisThread;

            List<Track> workForThisThread = allTracks.subList(startIdx, endIdx);

            if (workForThisThread.isEmpty()) {
                continue;
            }

            System.out.println("Creating Thread-" + i + " with " + workForThisThread.size() + " rows.");
            Runnable task = new InsertTask(workForThisThread, JDBC_URL, USERNAME, PASSWORD);
            Thread thread = new Thread(task, "Thread-" + i);
            thread.start();
            threads.add(thread);

            startIdx = endIdx;
        }

        System.out.println("All threads started. Main thread is waiting for completion...");

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All threads have finished. Program complete.");
    }
}
