package event;

public interface EventVisitor<T> {
    T visitRendezVous(RendezVousPersonnel rdv);
    T visitReunion(Reunion reunion);
    T visitPeriodique(EvenementPeriodique periodique);
}
