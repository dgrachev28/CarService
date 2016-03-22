package com.springapp.mvc.core;

import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
    Цех
 */
public class Workshop {
    private String name;
    private Set<Service> availableServices;
    private List<Master> masters;
    private Queue<Service> servicesQueue;

}
