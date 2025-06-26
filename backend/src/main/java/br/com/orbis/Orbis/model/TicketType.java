package br.com.orbis.Orbis.model;

public enum TicketType {
    FULL(1.0),
    HALF(0.5),
    VIP(2.0);

    private final double multiplier;

    TicketType(double multiplier) {
        this.multiplier = multiplier;
    }

    public double calculatePrice(double basePrice) {
        return basePrice * multiplier;
    }
}