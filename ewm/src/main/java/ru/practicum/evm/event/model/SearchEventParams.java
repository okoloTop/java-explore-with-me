package ru.practicum.evm.event.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.evm.event.enums.EventState;
import ru.practicum.evm.event.enums.SortValue;

import java.util.List;

@Getter
@Setter
@Builder
public class SearchEventParams {

    private Integer size;
    private Integer from;
    private List<Long> categories;
    private EventState states;
    private String rangeStart;
    private List<Long> users;
    private String rangeEnd;
    private Boolean onlyAvailable;
    private SortValue sort;
    private Boolean paid;
    private String text;

}
