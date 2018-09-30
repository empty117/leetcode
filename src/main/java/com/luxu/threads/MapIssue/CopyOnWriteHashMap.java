package com.luxu.threads.MapIssue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author xulu
 * @date 12/23/2017
 * allows originalHashMap can be read/write concurrently
 * readOnlyHashMap is immutable
 * writeOnCopyHashMap is thread private before stored in distributed cache(Infinispan)
 * @NotThreadSafe
 */
public class CopyOnWriteHashMap<K,V> implements Map<K,V>{
    private Map<K, V> readOnlyHashMap;
    private Map<K, V> writeOnCopyHashMap;

    public CopyOnWriteHashMap(Map<K, V> originalHashMap){
        setReadOnlyHashMap(originalHashMap);
    }

    public void setReadOnlyHashMap(Map<K, V> readOnlyHashMap) {
        this.readOnlyHashMap = readOnlyHashMap;
    }

    @Override
    public int size() {
        return writeOnCopyHashMap == null?readOnlyHashMap.size():writeOnCopyHashMap.size();
    }

    @Override
    public boolean isEmpty() {
        return writeOnCopyHashMap == null?readOnlyHashMap.isEmpty():writeOnCopyHashMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return writeOnCopyHashMap == null?readOnlyHashMap.containsKey(key):writeOnCopyHashMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return writeOnCopyHashMap == null?readOnlyHashMap.containsValue(value):writeOnCopyHashMap.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return writeOnCopyHashMap == null?readOnlyHashMap.get(key):writeOnCopyHashMap.get(key);
    }

    @Override
    public V put(K key, V value) {
        if(writeOnCopyHashMap == null){
            writeOnCopyHashMap = new HashMap<>();
            writeOnCopyHashMap.putAll(readOnlyHashMap);
        }
        return writeOnCopyHashMap.put(key,value);
    }

    @Override
    public V remove(Object key) {
        if(writeOnCopyHashMap == null){
            writeOnCopyHashMap = new HashMap<>();
            writeOnCopyHashMap.putAll(readOnlyHashMap);
        }
        return writeOnCopyHashMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if(writeOnCopyHashMap == null){
            writeOnCopyHashMap = new HashMap<>();
            writeOnCopyHashMap.putAll(readOnlyHashMap);
        }
        writeOnCopyHashMap.putAll(m);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported!");
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Not supported!");
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("Not supported!");
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Not supported!");
    }
}
