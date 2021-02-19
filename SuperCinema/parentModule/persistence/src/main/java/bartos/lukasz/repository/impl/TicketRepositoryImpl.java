package bartos.lukasz.repository.impl;

import bartos.lukasz.model.Ticket;
import bartos.lukasz.repository.TicketRepository;
import bartos.lukasz.repository.generic.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

@Repository
public class TicketRepositoryImpl extends AbstractCrudRepository<Ticket, Long> implements TicketRepository {
    public TicketRepositoryImpl(Jdbi connection) {
        super(connection);
    }
}
