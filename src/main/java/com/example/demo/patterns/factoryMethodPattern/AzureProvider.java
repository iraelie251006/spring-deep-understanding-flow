package com.example.demo.patterns.factoryMethodPattern;

public class AzureProvider extends CloudProvider{
    private final String provider = "Azure";
    @Override
    protected ComputeInstance createInstance(String instanceType) {
        return new ComputeInstance.Builder(instanceType, provider).region("us-west-1").build();
    }

    @Override
    protected String getProviderName() {
        return provider;
    }
}
