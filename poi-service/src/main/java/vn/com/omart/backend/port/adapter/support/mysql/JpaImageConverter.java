package vn.com.omart.backend.port.adapter.support.mysql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import vn.com.omart.backend.domain.model.Image;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.PropertyAccessor.*;

@Converter
@Slf4j
public class JpaImageConverter implements AttributeConverter<List<Image>, String> {

    // ObjectMapper is thread safe
    private final ObjectMapper objectMapper;

    public JpaImageConverter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.setVisibility(FIELD, ANY);
        this.objectMapper.setVisibility(IS_GETTER, NONE);
        this.objectMapper.setVisibility(GETTER, NONE);
        this.objectMapper.setVisibility(SETTER, NONE);
        this.objectMapper.setVisibility(CREATOR, NONE);
        this.objectMapper.setVisibility(CREATOR, NONE);
//        TypeResolverBuilder<?> typer = new ObjectMapper.DefaultTypeResolverBuilder(OBJECT_AND_NON_CONCRETE);
//        // we'll always use full class name, when using defaulting
//        typer = typer.init(JsonTypeInfo.Id.CLASS, null);
//        typer = typer.inclusion(JsonTypeInfo.As.PROPERTY);
//        typer = typer.typeProperty("_class");
//        this.objectMapper.setDefaultTyping(typer);
    }

    @Override
    public String convertToDatabaseColumn(List<Image> meta) {
        String jsonString = "";
        try {
            // convert list of POJO to json
            jsonString = objectMapper.writeValueAsString(meta);
//            log.debug("List-To-DBJson={}", jsonString);

        } catch (JsonProcessingException ex) {
            log.error(ex.getMessage());
        }
        return jsonString;
    }

    @Override
    public List<Image> convertToEntityAttribute(String dbData) {
        List<Image> list = new ArrayList<>();
        try {
            // convert json to list of POJO
            if(StringUtils.isBlank(dbData)){
                return list;
            }
            list = Arrays.asList(objectMapper.readValue(dbData, Image[].class));
            //list = Arrays.asList(objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(List.class, Image.class)));
            
//            log.debug("DBJson-To-List={}", list);

        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return list;
    }
}
