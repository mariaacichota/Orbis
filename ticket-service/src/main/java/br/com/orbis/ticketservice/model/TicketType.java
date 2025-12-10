package br.com.orbis.ticketservice.model;

public enum TicketType {
    FULL {
        @Override
        public double calculatePrice(double base) {
            return base;
        }
    },
    HALF {
        @Override
        public double calculatePrice(double base) {
            return base / 2;
        }
    },
    VIP {
        @Override
        public double calculatePrice(double base) {
            return base * 1.5;
        }
    };

    public abstract double calculatePrice(double base);
}