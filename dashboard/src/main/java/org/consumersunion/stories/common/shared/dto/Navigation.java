package org.consumersunion.stories.common.shared.dto;

public class Navigation {
    private String first;
    private String prev;
    private String next;
    private String last;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public static class Builder {
        private String first;
        private String prev;
        private String next;
        private String last;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder withFirst(String first) {
            this.first = first;
            return this;
        }

        public Builder withPrev(String prev) {
            this.prev = prev;
            return this;
        }

        public Builder withNext(String next) {
            this.next = next;
            return this;
        }

        public Builder withLast(String last) {
            this.last = last;
            return this;
        }

        public Navigation build() {
            Navigation navigation = new Navigation();
            navigation.setFirst(first);
            navigation.setPrev(prev);
            navigation.setNext(next);
            navigation.setLast(last);

            return navigation;
        }
    }
}
