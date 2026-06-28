package org.solocode.Corex.config;

@FunctionalInterface
public interface ConfigValueProvider {



    Object resolve(String path);
}
