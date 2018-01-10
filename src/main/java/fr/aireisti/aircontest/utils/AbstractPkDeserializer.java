package fr.aireisti.aircontest.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import org.hibernate.Session;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

public abstract class AbstractPkDeserializer<T> extends StdDeserializer<T> {
    public AbstractPkDeserializer() {
        this(null);
    }

    public AbstractPkDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Integer pk = jsonParser.readValueAs(Integer.class);

        Session session = HibernateUtil.getSessionFactory().openSession();

        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> cls = (Class<T>) superclass.getActualTypeArguments()[0];

        T item = session.get(cls, pk);
        session.close();
        return item;
    }
}
