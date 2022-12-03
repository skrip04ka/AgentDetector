package org.example;

import jade.core.AID;

public interface DetectorListener {
    void handle (String action, AID agent);
}
