package org.inserter;

public class Track {
    String artistName;
    String trackName;
    String trackId;
    int popularity;
    int year;
    String genre;
    float danceability;
    float energy;
    int key;
    float loudness;
    int mode;
    float speechiness;
    float acousticness;
    float instrumentalness;
    float liveness;
    float valence;
    float tempo;
    int durationMs;
    int timeSignature;

    private Track(String artistName, String trackName, String trackId, int popularity, int year, String genre,
            float danceability, float energy, int key, float loudness, int mode, float speechiness,
            float acousticness, float instrumentalness, float liveness, float valence, float tempo,
            int durationMs, int timeSignature) {
        this.artistName = artistName;
        this.trackName = trackName;
        this.trackId = trackId;
        this.popularity = popularity;
        this.year = year;
        this.genre = genre;
        this.danceability = danceability;
        this.energy = energy;
        this.key = key;
        this.loudness = loudness;
        this.mode = mode;
        this.speechiness = speechiness;
        this.acousticness = acousticness;
        this.instrumentalness = instrumentalness;
        this.liveness = liveness;
        this.valence = valence;
        this.tempo = tempo;
        this.durationMs = durationMs;
        this.timeSignature = timeSignature;
    }

    private static int safeInt(String s) {
        if (s == null || s.trim().isEmpty()) {
            return 0;
        }
        return Integer.parseInt(s.trim());
    }

    private static float safeFloat(String s) {
        if (s == null || s.trim().isEmpty()) {
            return 0.0f;
        }
        return Float.parseFloat(s.trim());
    }

    public static Track fromCsvLine(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length != 20) {
                System.err.println("Skipping malformed line: " + line);
                return null;
            }

            return new Track(
                    parts[1],
                    parts[2], // track_name
                    parts[3], // track_id
                    safeInt(parts[4]), // popularity
                    safeInt(parts[5]), // year
                    parts[6], // genre
                    safeFloat(parts[7]), // danceability
                    safeFloat(parts[8]), // energy
                    safeInt(parts[9]), // key
                    safeFloat(parts[10]), // loudness
                    safeInt(parts[11]), // mode
                    safeFloat(parts[12]), // speechiness
                    safeFloat(parts[13]), // acousticness
                    safeFloat(parts[14]), // instrumentalness
                    safeFloat(parts[15]), // liveness
                    safeFloat(parts[16]), // valence
                    safeFloat(parts[17]), // tempo
                    safeInt(parts[18]), // duration_ms
                    safeInt(parts[19]) // time_signature
            );
        } catch (Exception e) {
            System.err.println("Skipping line due to parse error: " + e.getMessage());
            return null;
        }
    }

    public String getArtistName() {
        return artistName;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getTrackId() {
        return trackId;
    }

    public int getPopularity() {
        return popularity;
    }

    public int getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public float getDanceability() {
        return danceability;
    }

    public float getEnergy() {
        return energy;
    }

    public int getKey() {
        return key;
    }

    public float getLoudness() {
        return loudness;
    }

    public int getMode() {
        return mode;
    }

    public float getSpeechiness() {
        return speechiness;
    }

    public float getAcousticness() {
        return acousticness;
    }

    public float getInstrumentalness() {
        return instrumentalness;
    }

    public float getLiveness() {
        return liveness;
    }

    public float getValence() {
        return valence;
    }

    public float getTempo() {
        return tempo;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public int getTimeSignature() {
        return timeSignature;
    }
}
