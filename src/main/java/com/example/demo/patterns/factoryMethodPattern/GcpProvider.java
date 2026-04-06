package com.example.demo.patterns.factoryMethodPattern;

public class GcpProvider extends CloudProvider{
    private final String provider = "GCP";
    @Override
    protected ComputeInstance createInstance(String instanceType) {
        return new ComputeInstance.Builder(instanceType, provider).region("us-north-2").build();
    }

    @Override
    protected String getProviderName() {
        return provider;
    }
}
