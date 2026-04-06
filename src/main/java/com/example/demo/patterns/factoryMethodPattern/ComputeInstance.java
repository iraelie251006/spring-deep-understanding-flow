package com.example.demo.patterns.factoryMethodPattern;

import java.util.Objects;
import java.util.UUID;

public final class ComputeInstance {
    private final String instanceId;
    private final String instanceType;
    private final String provider;
    private final String region;

    private ComputeInstance(Builder builder) {
        this.instanceId = builder.instanceId;
        this.instanceType = builder.instanceType;
        this.provider = builder.provider;
        this.region = builder.region;
    }

    public static final class Builder {
        private String instanceId;
        private String instanceType;
        private String provider;
        private String region = "us-east-1";

        public Builder(String instanceType, String provider) {
            this.instanceId = UUID.randomUUID().toString();

            if (instanceType == null || instanceType.isBlank()) {
                throw new IllegalArgumentException("instanceType is required");
            }

            if (provider == null || provider.isBlank()) {
                throw new IllegalArgumentException("provider is required");
            }

            this.instanceType = instanceType;
            this.provider = provider;
        }

        public Builder region(String region) {
            this.region = Objects.requireNonNull(region, "region is required.");
            return this;
        }

        public ComputeInstance build() {
            return new ComputeInstance(this);
        }
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public String getProvider() {
        return provider;
    }

    public String getRegion() {
        return region;
    }
}
