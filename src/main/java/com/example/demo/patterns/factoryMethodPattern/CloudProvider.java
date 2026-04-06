package com.example.demo.patterns.factoryMethodPattern;

public abstract class CloudProvider {
    protected abstract ComputeInstance createInstance(String instanceType);
    protected abstract String getProviderName();

    public ComputeInstance provision(String instanceType) {
        System.out.println("Provisioning on " + getProviderName());

        return createInstance(instanceType);
    }
}
