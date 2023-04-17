package org.acme;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerStatusEventService
{
    public void process() {
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
