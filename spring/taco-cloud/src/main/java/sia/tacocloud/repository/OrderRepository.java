package sia.tacocloud.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import sia.tacocloud.domain.TacoOrder;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends CrudRepository<TacoOrder, Long> {
    List<TacoOrder> findByDeliveryZip(String deliveryZip);

    List<TacoOrder> readOrdersByDeliveryZipAndPlacedAtBetween(String deliveryZip, LocalDate startDate,
                                                              LocalDate endDate);

    List<TacoOrder> findByDeliveryStreetAndDeliveryCityAllIgnoreCase(String deliveryStreet, String deliveryCity);

    List<TacoOrder> findByDeliveryCityOrderByDeliveryCity(String city);

    @Query("SELECT o FROM TacoOrder o WHERE o.deliveryCity = 'Seattle'")
    List<TacoOrder> readOrdersDeliveredInSeattle();

}
