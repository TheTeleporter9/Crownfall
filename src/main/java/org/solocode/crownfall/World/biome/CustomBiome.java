package org.solocode.crownfall.World.biome;

public class CustomBiome {

    private final String id;

    private final String resourceKey;
    private final String fogColor;
    private final String foliageColor;
    private final String skyColor;
    private final String waterColor;
    private final String waterFogColor;

    public CustomBiome(
            String id,
            String resourceKey,
            String fogColor,
            String foliageColor,
            String skyColor,
            String waterColor,
            String waterFogColor
    ) {
        this.id = id;
        this.resourceKey = resourceKey;
        this.fogColor = fogColor;
        this.foliageColor = foliageColor;
        this.skyColor = skyColor;
        this.waterColor = waterColor;
        this.waterFogColor = waterFogColor;
    }

    public String getId() {
        return id;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public String getFogColor() {
        return fogColor;
    }

    public String getFoliageColor() {
        return foliageColor;
    }

    public String getSkyColor() {
        return skyColor;
    }

    public String getWaterColor() {
        return waterColor;
    }

    public String getWaterFogColor() {
        return waterFogColor;
    }
}