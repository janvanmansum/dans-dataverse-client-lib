package nl.knaw.dans.lib.dataverse;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import nl.knaw.dans.lib.dataverse.model.DataverseEnvelope2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataverseResponse2<D> extends DataverseComplexMessageResponse<String, D> {

    public DataverseResponse2(String bodyText, ObjectMapper mapper, Class<?>... typeParameterClasses) {
        super(bodyText, mapper, prepend(String.class, typeParameterClasses));
    }

    private static Class<?>[] prepend(Class<?> c, Class<?>[] a) {
        ArrayList<Class<?>> list = new ArrayList<>();
        list.add(c);
        list.addAll(Arrays.asList(a));
        return list.toArray(new Class<?>[] {});
    }

}
