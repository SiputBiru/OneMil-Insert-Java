package org.inserter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class InsertTask implements Runnable {

    private final List<Track> workQueue;
    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final int BATCH_SIZE = 1000;

    public InsertTask(List<Track> workQueue, String jdbcUrl, String username, String password) {
        this.workQueue = workQueue;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();

        String sql = "INSERT INTO music_tracks (artist_name, track_name, track_id, " +
                "popularity, year, genre, danceability, energy, key, loudness, mode, " +
                "speechiness, acousticness, instrumentalness, liveness, valence, " +
                "tempo, duration_ms, time_signature) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
                PreparedStatement statement = connection.prepareStatement(sql)) {

            System.out.println(threadName + " connected to DB.");
            connection.setAutoCommit(false);

            int itemsInCurrentBatch = 0;
            for (Track track : workQueue) {

                // --- Set all 19 parameters ---
                statement.setString(1, track.getArtistName());
                statement.setString(2, track.getTrackName());
                statement.setString(3, track.getTrackId());
                statement.setInt(4, track.getPopularity());
                statement.setInt(5, track.getYear());
                statement.setString(6, track.getGenre());
                statement.setFloat(7, track.getDanceability());
                statement.setFloat(8, track.getEnergy());
                statement.setInt(9, track.getKey());
                statement.setFloat(10, track.getLoudness());
                statement.setInt(11, track.getMode());
                statement.setFloat(12, track.getSpeechiness());
                statement.setFloat(13, track.getAcousticness());
                statement.setFloat(14, track.getInstrumentalness());
                statement.setFloat(15, track.getLiveness());
                statement.setFloat(16, track.getValence());
                statement.setFloat(17, track.getTempo());
                statement.setInt(18, track.getDurationMs());
                statement.setInt(19, track.getTimeSignature());

                statement.addBatch();
                itemsInCurrentBatch++;

                if (itemsInCurrentBatch % BATCH_SIZE == 0) {
                    statement.executeBatch();
                    connection.commit();
                    System.out.println(threadName + " inserted " + BATCH_SIZE + " rows.");
                }
            }

            statement.executeBatch();
            connection.commit();

            System.out.println(threadName + " inserted final batch. Total: " + workQueue.size());

        } catch (SQLException e) {
            System.err.println(threadName + " failed: " + e.getMessage());
            // This is useful for debugging duplicate track_id issues
            if (e.getNextException() != null) {
                e.getNextException().printStackTrace();
            }
        }
        System.out.println(threadName + " finished and disconnected.");
    }
}
