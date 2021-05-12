package me.gv7.woodpecker.requests.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provider json ability with gson
 *
 * @author Liu Dong
 */
public class GsonProcessor implements JsonProcessor {
    private static final Logger logger = Logger.getLogger(GsonProcessor.class.getName());
    private final Gson gson;

    public GsonProcessor() {
        this(getDefaultGson());
    }

    private static Gson getDefaultGson() {
        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
        registerAllTypeFactories(gsonBuilder);
        return gsonBuilder.create();
    }

    /**
     * Find and register all gson type factory using spi
     */
    private static void registerAllTypeFactories(GsonBuilder gsonBuilder) {
        ServiceLoader<TypeAdapterFactory> loader = ServiceLoader.load(TypeAdapterFactory.class);
        for (TypeAdapterFactory typeFactory : loader) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Add gson type factory: " + typeFactory.getClass().getName());
            }
            gsonBuilder.registerTypeAdapterFactory(typeFactory);
        }
    }

    public GsonProcessor(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void marshal(Writer writer, Object value) {
        gson.toJson(value, writer);
    }

    @Override
    public <T> T unmarshal(InputStream inputStream, Charset charset, Type type) throws IOException {
        try (Reader reader = new InputStreamReader(inputStream, charset)) {
            return gson.fromJson(reader, type);
        }
    }
}
