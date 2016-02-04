package org.consumersunion.stories.server.api.rest.converters;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

public abstract class AbstractConverter<I, O> {
    public static class PendingConverter<I, O> implements Iterable<O> {
        private final AbstractConverter<I, O> converter;
        private final Iterable<I> iterable;

        private PendingConverter(
                AbstractConverter<I, O> converter,
                Iterable<I> iterable) {
            this.converter = converter;
            this.iterable = iterable;
        }

        public List<O> asList() {
            return Lists.newArrayList(this);
        }

        @Override
        public Iterator<O> iterator() {
            return Iterators.filter(unfilteredIterator(), Predicates.notNull());
        }

        private Iterator<O> unfilteredIterator() {
            return new Iterator<O>() {
                private final Iterator<I> iterator = iterable.iterator();

                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public O next() {
                    return converter.convert(iterator.next());
                }

                @Override
                public void remove() {
                    iterator.remove();
                }
            };
        }
    }

    public abstract O convert(I input);

    public PendingConverter<I, O> convertAll(I... input) {
        return convertAll(Arrays.asList(input));
    }

    public PendingConverter<I, O> convertAll(Iterable<I> input) {
        return new PendingConverter<I, O>(this, input);
    }
}
