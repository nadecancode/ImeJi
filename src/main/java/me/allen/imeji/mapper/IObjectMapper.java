package me.allen.imeji.mapper;

import com.google.gson.JsonObject;

public interface IObjectMapper<T> {

    JsonObject toJsonObject(T t);

    T fromJsonObject(JsonObject jsonObject);

}
