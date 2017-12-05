package fr.aireisti.aircontest.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import org.hibernate.Session;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractPkListDeserializer<T> extends StdDeserializer<Set<T>> {
    public AbstractPkListDeserializer() {
        this(null);
    }

    public AbstractPkListDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Set<T> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Integer[] pkList = jsonParser.readValueAs(Integer[].class);
        Set<T> itemSet = new HashSet<>();

        Session session = HibernateUtil.getSessionFactory().openSession();

        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> cls = (Class<T>) superclass.getActualTypeArguments()[0];

        for (Integer pk : pkList) {
            T item = session.get(cls, pk);
            itemSet.add(item);
        }
        session.close();
        return itemSet;
    }
}
