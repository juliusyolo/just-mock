package com.sdefaa.just.mock.test.pojo;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class Wrapper<T> {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
