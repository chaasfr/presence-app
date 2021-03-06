package nl.dobots.presence;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit.converter.ConversionException;
import retrofit.converter.JacksonConverter;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

/**
 * Created by Jordi on 29-9-2014.
 */
public class StandByJacksonConverter extends JacksonConverter {

    private static final String MIME_TYPE = "application/json; charset=UTF-8";

    private final ObjectMapper objectMapper;

    public StandByJacksonConverter() {
        this(new ObjectMapper());
    }

    public StandByJacksonConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructType(type);
            return objectMapper.readValue(body.in(), javaType);
        }
        catch (JsonParseException e) {
            throw new ConversionException(e);
        }
        catch (JsonMappingException e) {
            throw new ConversionException(e);
        }
        catch (IOException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public TypedOutput toBody(Object object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            return new TypedByteArray(MIME_TYPE, json.getBytes("UTF-8"));
        }
        catch (JsonProcessingException e) {
            throw new AssertionError(e);
        }
        catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
}
