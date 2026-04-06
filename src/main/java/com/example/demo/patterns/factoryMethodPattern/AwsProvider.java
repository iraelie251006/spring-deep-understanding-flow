package com.example.demo.patterns.factoryMethodPattern;

public class AwsProvider extends CloudProvider{
    private final String provider = "AWS";
    @Override
    protected ComputeInstance createInstance(String instanceType) {
        return new ComputeInstance.Builder(instanceType, provider).region("us-east-1").build();
    }

    @Override
    protected String getProviderName() {
        return provider;
    }
}
