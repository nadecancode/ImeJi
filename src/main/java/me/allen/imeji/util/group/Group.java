package me.allen.imeji.util.group;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Group<A, B> {
    private A a;
    private B b;
}
