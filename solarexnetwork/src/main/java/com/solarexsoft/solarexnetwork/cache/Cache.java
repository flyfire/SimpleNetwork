package com.solarexsoft.solarexnetwork.cache;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 09/06/2017
 *    Desc:
 * </pre>
 */

public interface Cache<K,V> {
    public V get(K key);
    public void put(K key, V value);
    public void remove(K key);
}
