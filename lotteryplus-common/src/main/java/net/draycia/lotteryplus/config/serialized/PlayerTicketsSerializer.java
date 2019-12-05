package net.draycia.lotteryplus.config.serialized;

import com.google.common.reflect.TypeToken;
import net.draycia.lotteryplus.datatypes.PlayerTickets;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

public class PlayerTicketsSerializer implements TypeSerializer<PlayerTickets> {

    private static final String UNIQUE_ID = "Unique-ID";
    private static final String TICKETS_PURCHASED = "Tickets-Purchased";

    /**
     * Deserialize an object (of the correct type) from the given configuration node.
     *
     * @param type  The type of return value required
     * @param value The node containing serialized data
     * @return An object
     * @throws ObjectMappingException If the presented data is invalid
     */
    @Nullable
    @Override
    public PlayerTickets deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
        //UUIDs are fussy
        UUID uuid;

        try {
            uuid = UUID.fromString(value.getNode(UNIQUE_ID).getString());
        } catch (IllegalArgumentException ex) {
            throw new ObjectMappingException("Value not a UUID", ex);
        }

        Integer ticketAmount = value.getNode(TICKETS_PURCHASED).getValue(TypeToken.of(Integer.class));

        return PlayerTickets.fromData(uuid, ticketAmount);
    }

    /**
     * Serialize an object to the given configuration node.
     *
     * @param type  The type of the input object
     * @param obj   The object to be serialized
     * @param value The node to write to
     * @throws ObjectMappingException If the object cannot be serialized
     */
    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable PlayerTickets obj, @NonNull ConfigurationNode value) throws ObjectMappingException {
        value.getNode(UNIQUE_ID).setValue(obj.getUUID().toString());
        value.getNode(TICKETS_PURCHASED).setValue(obj.getTicketPurchaseCount());
    }
}
