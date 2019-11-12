package me.allen.imeji.task.type;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class ImeJiTask {
    private final int ticks;
    private final Runnable task;
}
