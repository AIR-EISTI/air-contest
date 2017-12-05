package fr.aireisti.aircontest.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;  
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import fr.aireisti.aircontest.Hibernate.HibernateUtil;
import fr.aireisti.aircontest.models.Tag;
import org.hibernate.Session;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PkListDeserializer extends StdDeserializer<Set<Tag>> {
    public PkListDeserializer() {
        this(null);
    }

    public PkListDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Set<Tag> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Integer[] pkList = jsonParser.readValueAs(Integer[].class);
        Set<Tag> itemSet = new HashSet<Tag>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        for (Integer pk : pkList) {
            Tag item = session.get(Tag.class, pk);
            itemSet.add(item);
        }
        session.close();
        return itemSet;
    }
}
