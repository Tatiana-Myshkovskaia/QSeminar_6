package ru.sberbank.service;

import ru.sberbank.data.Developer;
import ru.sberbank.data.TeamMember;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStorage<T extends TeamMember> {
    protected List<T> list;

    protected AbstractStorage(){
        this.list = new ArrayList<>();
    }

    public abstract void add(TeamMember teamMember);
}
