package main;

public class Fish {

    private String species;
    private Double length;
    private Double size;
    private Double weight;
    private Double infestationRate;

    public Fish(String species) {
        this.species = species;
    }

    public Fish(String species, Double length, Double size, Double weight, Double infestationRate) {
        this.species = species;
        this.length = length;
        this.size = size;
        this.weight = weight;
        this.infestationRate = infestationRate;
    }

}
