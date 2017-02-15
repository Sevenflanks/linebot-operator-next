package next.operator.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class AbstractDao<TYPE> {

    private final Set<TYPE> datas = Sets.newHashSet();

    public Set<TYPE> findAll() {
        return datas;
    }

    public synchronized void insert(TYPE entity) {
        datas.add(entity);
    }

}
