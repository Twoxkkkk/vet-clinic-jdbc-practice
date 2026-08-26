package application.vet.dto;

import domain.shared.Id;
import domain.vet.Vet;

public record VetPerformanceDto(
    Id<Vet> vetId,
    String vetInitials,

    int appointmentsCount,
    double totalRevenue
) {}
