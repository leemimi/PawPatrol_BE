package com.patrol.domain.Postable;

public interface Postable {

    Long getId();

    default String getPostType() {
        return this.getClass().getSimpleName();
    }
}