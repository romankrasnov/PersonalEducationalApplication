package com.smallredtracktor.yourpersonaleducationalapplication.main.LocalDataSources;

import java.util.List;

public interface ICreateTestRootDbApi {
    void writeSubject(String id, String name);
    void writeTickets(List<String> strings, String id);
}
