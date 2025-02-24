package ws.adaptors;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationTypeAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (duration == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(String.valueOf(duration.toMinutes()));
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == null) {
            return Duration.ofMinutes(0);
        }
        return Duration.ofMinutes(Long.parseLong(jsonReader.nextString()));
    }
}
