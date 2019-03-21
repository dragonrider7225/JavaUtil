package util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.DefaultLocation;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * This map uses another map as its default mappings. That is, if this map was created with a map
 * <code>{"A": 3, "B": 4}</code> and was subsequently called with <code>this.put("A", 5)</code>,
 * this map looks like <code>{"A": 5, "B": 4}</code> and <code>this.remove("A")</code> will return
 * it to <code>{"A": 3, "B": 4}</code>. Changes to the backing map will be reflected in this map
 * except where such changes are hidden by calls to {@link #put(Object, Object) this.put(K, V)}.
 * @param <K> the key type
 * @param <V> the value type
 */
@NonNullByDefault({})
public class WrapperMap<K, V> implements Map<K, V> {
    private final Map<K, V> back;
    private final Map<K, V> others = new HashMap<>();

    /**
     * @param back the mappings that this map can't remove
     */
    public WrapperMap(final Map<? extends K, ? extends V> back) {
        this.back = Collections.unmodifiableMap(back);
    }

    @Override
    public int size() {
        final HashSet<K> keys = new HashSet<>(this.back.keySet());
        keys.addAll(this.others.keySet());
        return keys.size();
    }

    @Override
    public boolean isEmpty() {
        return this.back.isEmpty() && this.others.isEmpty();
    }

    @SuppressWarnings("unlikely-arg-type")
    @Override
    public boolean containsKey(@Nullable final Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Expected Key, found null"); //$NON-NLS-1$
        }
        return this.back.containsKey(key) || this.others.containsKey(key);
    }

    @SuppressWarnings("unlikely-arg-type")
    @Override
    public boolean containsValue(@Nullable final Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Expected Value, found null"); //$NON-NLS-1$
        }
        return this.back.containsValue(value) || this.others.containsValue(value);
    }

    @SuppressWarnings("unlikely-arg-type")
    @Override
    public V get(@Nullable final Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Expected Key, found null"); //$NON-NLS-1$
        }
        return (this.others.containsKey(key) ? this.others : this.back).get(key);
    }

    @Override
    public V put(@Nullable final K key, @Nullable final V value) {
        if (key == null) {
            throw new IllegalArgumentException("Expected Key, found null"); //$NON-NLS-1$
        }
        if (value == null) {
            throw new IllegalArgumentException("Expected Value, found null"); //$NON-NLS-1$
        }
        final V ret = this.get(key);
        this.others.put(key, value);
        return ret;
    }

    @SuppressWarnings("unlikely-arg-type")
    @Override
    public V remove(@Nullable final Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Expected Key, found null"); //$NON-NLS-1$
        }
        final V ret = this.get(key);
        this.others.remove(key);
        return ret;
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        this.others.putAll(m);
    }

    @Override
    public void clear() {
        this.others.clear();
    }

    @Override
    public Set<K> keySet() {
        final Map<K, V> backLocal = this.back;
        final Map<K, V> othersLocal = this.others;
        return new Set<>() {
            @Override
            public int size() {
                return WrapperMap.this.size();
            }

            @Override
            public boolean isEmpty() {
                return WrapperMap.this.isEmpty();
            }

            @SuppressWarnings("unlikely-arg-type")
            @Override
            public boolean contains(@Nullable final Object o) {
                return WrapperMap.this.containsKey(o);
            }

            private Set<K> uselessKeys() {
                final Set<K> ret = new HashSet<>(backLocal.keySet());
                ret.addAll(othersLocal.keySet());
                return ret;
            }

            @SuppressWarnings("null")
            @Override
            public Iterator<K> iterator() {
                final Set<K> backKeys = new HashSet<>(backLocal.keySet());
                final Set<K> othersKeys = othersLocal.keySet();
                backKeys.removeAll(othersKeys);
                return Iterators.concat(backKeys.iterator(), othersKeys.iterator());
            }

            @Override
            public Object[] toArray() {
                return this.uselessKeys().toArray();
            }

            @Override
            public <T> T[] toArray(final T @Nullable [] a) {
                return this.uselessKeys().toArray(a);
            }

            @Override
            public boolean add(final K e) {
                return othersLocal.keySet().add(e);
            }

            @SuppressWarnings("unlikely-arg-type")
            @Override
            public boolean remove(@Nullable final Object o) {
                return othersLocal.keySet().remove(o);
            }

            @SuppressWarnings("unlikely-arg-type")
            @Override
            public boolean containsAll(@Nullable final Collection<?> c) {
                return this.uselessKeys().containsAll(c);
            }

            @Override
            public boolean addAll(@Nullable final Collection<? extends K> c) {
                return othersLocal.keySet().addAll(c);
            }

            @SuppressWarnings("unlikely-arg-type")
            @Override
            public boolean retainAll(@Nullable final Collection<?> c) {
                return othersLocal.keySet().retainAll(c);
            }

            @SuppressWarnings("unlikely-arg-type")
            @Override
            public boolean removeAll(@Nullable final Collection<?> c) {
                return othersLocal.keySet().removeAll(c);
            }

            @Override
            public void clear() {
                othersLocal.keySet().clear();
            }
        };
    }

    @Override
    public Collection<V> values() {
        final Map<K, V> backLocal = this.back;
        final Map<K, V> othersLocal = this.others;
        return new Collection<>() {
            @Override
            public int size() {
                return WrapperMap.this.size();
            }

            @Override
            public boolean isEmpty() {
                return WrapperMap.this.isEmpty();
            }

            @SuppressWarnings("unlikely-arg-type")
            @Override
            public boolean contains(@Nullable final Object o) {
                return WrapperMap.this.containsValue(o);
            }

            @Override
            public Iterator<V> iterator() {
                final Iterator<Entry<K, V>> iter = WrapperMap.this.entrySet().iterator();
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    @Override
                    public V next() {
                        return iter.next().getValue();
                    }

                    @Override
                    public void remove() {
                        iter.remove();
                    }
                };
            }

            @Override
            public Object[] toArray() {
                return WrapperMap.this.entrySet().stream().map(Entry::getValue).toArray();
            }

            @SuppressWarnings({"unchecked", "null"})
            @Override
            public <T> T[] toArray(final T @Nullable [] a) {
                T[] ret = a;
                final int size = this.size();
                if (ret == null || ret.length < size) {
                    ret = (T[]) new Object[size];
                }
                System.arraycopy(this.toArray(), 0, ret, 0, size);
                if (ret.length > size) {
                    ret[size] = null;
                }
                return ret;
            }

            @Override
            public boolean add(final V e) {
                return othersLocal.values().add(e);
            }

            @SuppressWarnings("unlikely-arg-type")
            @Override
            public boolean remove(@Nullable final Object o) {
                return othersLocal.values().remove(o);
            }

            @SuppressWarnings("unlikely-arg-type")
            @Override
            public boolean containsAll(@Nullable final Collection<?> c) {
                return WrapperMap.this.entrySet().stream().map(Entry::getValue).collect(Collectors.toList()).containsAll(c);
            }

            @Override
            public boolean addAll(@Nullable final Collection<? extends V> c) {
                return othersLocal.values().addAll(c);
            }

            @Override
            public boolean removeAll(@Nullable final Collection<?> c) {
                if (c == null) {
                    throw new NullPointerException();
                }
                boolean modified = false;
                for (final Object o : c) {
                    final Optional<Entry<K, V>> maybeKVPair = othersLocal.entrySet().stream().filter(e -> e.getValue() == o).findFirst();
                    if (!maybeKVPair.isPresent()) {
                        continue;
                    }
                    final Entry<K, V> kvPair = maybeKVPair.get();
                    if (backLocal.get(kvPair.getKey()) != othersLocal.get(kvPair.getKey())) {
                        othersLocal.remove(kvPair.getKey());
                        modified = true;
                    }
                }
                return modified;
            }

            @SuppressWarnings("unlikely-arg-type")
            @Override
            public boolean retainAll(@Nullable final Collection<?> c) {
                return othersLocal.values().retainAll(c);
            }

            @Override
            public void clear() {
                othersLocal.values().clear();
            }
        };
    }

    @NonNullByDefault(DefaultLocation.RETURN_TYPE)
    @Override
    public Set<Entry<K, V>> entrySet() {
        final Map<K, V> backLocal = this.back;
        final Map<K, V> othersLocal = this.others;
        return new Set<>() {
            @Override
            public int size() {
                return WrapperMap.this.size();
            }

            @Override
            public boolean isEmpty() {
                return WrapperMap.this.isEmpty();
            }

            @SuppressWarnings("unchecked")
            @Override
            public boolean contains(@Nullable final Object o) {
                if (!(o instanceof Entry)) {
                    return false;
                }
                final Entry<K, V> e = (Entry<K, V>) o;
                return (othersLocal.containsKey(e.getKey()) ? othersLocal : backLocal).entrySet().contains(e);
            }

            @SuppressWarnings("null")
            @Override
            public Iterator<Entry<K, V>> iterator() {
                final Set<Entry<K, V>> backEntries = new HashSet<>(backLocal.entrySet());
                final Set<Entry<K, V>> otherEntries = othersLocal.entrySet();
                for (final Entry<K, V> otherEntry : otherEntries) {
                    for (final Entry<K, V> backEntry : backEntries) {
                        if (otherEntry.getKey() == backEntry.getKey()) {
                            backEntries.remove(backEntry);
                        }
                    }
                }
                return Iterators.concat(backEntries.iterator(), otherEntries.iterator());
            }

            @SuppressWarnings("null")
            @Override
            public Object[] toArray() {
                return this.stream().toArray();
            }

            @SuppressWarnings({"unchecked", "null"})
            @Override
            public <T> T[] toArray(final T @Nullable [] a) {
                T[] ret = a;
                final int size = this.size();
                if (ret == null || ret.length < size) {
                    ret = (T[]) new Object[size];
                }
                System.arraycopy(this.toArray(), 0, ret, 0, size);
                if (ret.length > size) {
                    ret[size] = null;
                }
                return ret;
            }

            @Override
            public boolean add(final Entry<K, V> e) {
                return othersLocal.entrySet().add(e);
            }

            @SuppressWarnings("unlikely-arg-type")
            @Override
            public boolean remove(@Nullable final Object o) {
                return othersLocal.entrySet().remove(o);
            }

            @SuppressWarnings("unlikely-arg-type")
            @Override
            public boolean containsAll(@Nullable final Collection<?> c) {
                if (c == null) {
                    throw new NullPointerException();
                }
                for (final Object o : c) {
                    if (!backLocal.entrySet().contains(o) && !othersLocal.entrySet().contains(o)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean addAll(@Nullable final Collection<? extends Entry<K, V>> c) {
                return othersLocal.entrySet().addAll(c);
            }

            @SuppressWarnings("unlikely-arg-type")
            @Override
            public boolean retainAll(@Nullable final Collection<?> c) {
                return othersLocal.entrySet().retainAll(c);
            }

            @SuppressWarnings("unlikely-arg-type")
            @Override
            public boolean removeAll(@Nullable final Collection<?> c) {
                return othersLocal.entrySet().removeAll(c);
            }

            @Override
            public void clear() {
                othersLocal.entrySet().clear();
            }
        };
    }
}
